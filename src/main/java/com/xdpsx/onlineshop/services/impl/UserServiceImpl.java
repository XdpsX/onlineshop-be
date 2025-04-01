package com.xdpsx.onlineshop.services.impl;

import org.springframework.stereotype.Service;

import com.xdpsx.onlineshop.dtos.user.UserProfile;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.mappers.UserMapper;
import com.xdpsx.onlineshop.repositories.UserRepository;
import com.xdpsx.onlineshop.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfile getUserByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email=%s not found".formatted(email)));
        return userMapper.fromEntityToProfile(user);
    }
}
