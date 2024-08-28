package com.xdpsx.ecommerce.mappers;

import com.xdpsx.ecommerce.dtos.user.UserResponse;
import com.xdpsx.ecommerce.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse fromEntityToResponse(User entity);
}
