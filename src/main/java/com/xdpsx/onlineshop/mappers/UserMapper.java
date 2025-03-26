package com.xdpsx.onlineshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.xdpsx.onlineshop.dtos.user.UserProfile;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.enums.AuthProvider;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private CloudinaryUploader uploader;

    @Mapping(target = "role", ignore = true)
    abstract UserProfile buildUserProfile(User entity);

    public UserProfile fromEntityToProfile(User entity) {
        UserProfile response = buildUserProfile(entity);
        if (entity.getAuthProvider().equals(AuthProvider.LOCAL)) {
            response.setAvatarUrl(uploader.getFileUrl(entity.getAvatar()));
        } else {
            response.setAvatarUrl(entity.getAvatar());
        }
        response.setRole(entity.getRole().name());
        return response;
    }
}
