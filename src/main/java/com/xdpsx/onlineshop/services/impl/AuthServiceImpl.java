package com.xdpsx.onlineshop.services.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.mail.MessagingException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xdpsx.onlineshop.constants.AuthConstants;
import com.xdpsx.onlineshop.constants.CacheKey;
import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.dtos.auth.LoginRequest;
import com.xdpsx.onlineshop.dtos.auth.RegisterRequest;
import com.xdpsx.onlineshop.dtos.auth.SendOtpRequest;
import com.xdpsx.onlineshop.dtos.auth.TokenResponse;
import com.xdpsx.onlineshop.entities.Role;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.AuthProvider;
import com.xdpsx.onlineshop.entities.enums.EmailTemplate;
import com.xdpsx.onlineshop.entities.enums.OTPKey;
import com.xdpsx.onlineshop.entities.enums.RoleName;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.exceptions.TooManyRequestsException;
import com.xdpsx.onlineshop.repositories.RoleRepository;
import com.xdpsx.onlineshop.repositories.UserRepository;
import com.xdpsx.onlineshop.security.CustomUserDetails;
import com.xdpsx.onlineshop.security.TokenProvider;
import com.xdpsx.onlineshop.services.AuthService;
import com.xdpsx.onlineshop.utils.EmailSender;
import com.xdpsx.onlineshop.utils.SecurityCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final RedisTemplate<String, String> redisTemplateString;
    private final EmailSender emailSender;

    @Override
    public String register(RegisterRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);

        // User already exists
        if (user != null) {
            // User is already verified
            if (user.isEnabled()) {
                throw new DuplicateException(EMessage.DATA_EXISTS, request.email());
            } else { // User is not verified
                user.setName(request.name());
                user.setPassword(passwordEncoder.encode(request.password()));
                userRepository.save(user);
            }
        } else { // User does not exist
            Role userRole = roleRepository
                    .findByName(RoleName.USER)
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, "Role User"));
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User newUser = User.builder()
                    .email(request.email())
                    .name(request.name())
                    .password(passwordEncoder.encode(request.password()))
                    .enabled(false)
                    .roles(roles)
                    .authProvider(AuthProvider.SYSTEM)
                    .build();
            userRepository.save(newUser);
        }
        return request.email();
    }

    @Override
    public void sendVerifyEmailOTP(SendOtpRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.email()));
        if (user.isEnabled()) {
            throw new BadRequestException(EMessage.EMAIL_VERIFIED, request.email());
        }

        String cooldownKey = CacheKey.buildOtpKey(OTPKey.COOLDOWN_VERIFY_EMAIL, request.email());
        String dailyCountKey = CacheKey.buildOtpKey(OTPKey.DAILY_COUNT_VERIFY_EMAIL, request.email());
        String otpVerifyEmailKey = CacheKey.buildOtpKey(OTPKey.OTP_VERIFY_EMAIL, request.email());

        // 1. Cooldown 60s
        if (Boolean.TRUE.equals(redisTemplateString.hasKey(cooldownKey))) {
            throw new TooManyRequestsException(EMessage.OTP_COOLDOWN);
        }

        // 2. Max 5 times/day
        Long count = redisTemplateString.opsForValue().increment(dailyCountKey);
        if (count != null && count > AuthConstants.VERIFY_EMAIL_DAILY_COUNT) {
            throw new TooManyRequestsException(EMessage.OTP_DAILY_LIMIT_EXCEEDED);
        }
        redisTemplateString.expire(dailyCountKey, Duration.ofDays(1));

        // 3. Generate OTP and store in Redis
        String otp = SecurityCodeGenerator.generateNumericCode(AuthConstants.VERIFY_EMAIL_OTP_LENGTH);
        redisTemplateString
                .opsForValue()
                .set(otpVerifyEmailKey, otp, AuthConstants.VERIFY_EMAIL_OTP_TTL_MINUTES, TimeUnit.MINUTES);

        // 4. Set cooldown
        redisTemplateString
                .opsForValue()
                .set(cooldownKey, "true", AuthConstants.VERIFY_EMAIL_COOLDOWN_MINUTES, TimeUnit.MINUTES);

        // 5. Send email
        Map<String, Object> mailProps = new HashMap<>();
        mailProps.put("otp", otp);
        mailProps.put("ttl", AuthConstants.VERIFY_EMAIL_OTP_TTL_MINUTES);

        try {
            emailSender.sendEmail(request.email(), EmailTemplate.VERIFY_EMAIL, mailProps);
        } catch (MessagingException e) {
            //            redisTemplateString.delete(cacheKey);
            throw new BadRequestException(EMessage.EMAIL_SEND_FAILED, request.email());
        }
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(user);
        return TokenResponse.builder().accessToken(accessToken).build();
    }
}
