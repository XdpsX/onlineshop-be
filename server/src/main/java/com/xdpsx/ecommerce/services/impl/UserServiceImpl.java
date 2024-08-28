package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.user.UserResponse;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.mappers.UserMapper;
import com.xdpsx.ecommerce.repositories.UserRepository;
import com.xdpsx.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse getUserProfile(Principal loggedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) loggedUser).getPrincipal();
        return userMapper.fromEntityToResponse(user);
    }
}
