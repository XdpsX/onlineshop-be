package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.user.UserProfile;

public interface UserService {
    UserProfile getUserByEmail(String email);
}
