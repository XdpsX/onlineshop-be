package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.user.UserProfile;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private CloudinaryUploader uploader;

    @Mapping(target = "role", ignore = true)
    abstract UserProfile buildUserProfile(User entity);

    public UserProfile fromEntityToProfile(User entity){
        UserProfile response = buildUserProfile(entity);
        response.setAvatarUrl(uploader.getFileUrl(entity.getAvatar()));
        response.setRole(entity.getRole().name());
        return response;
    }
}
