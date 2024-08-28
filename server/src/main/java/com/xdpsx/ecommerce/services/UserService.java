package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.user.UserResponse;

import java.security.Principal;

public interface UserService {
    UserResponse getUserProfile(Principal loggedUser);
}
