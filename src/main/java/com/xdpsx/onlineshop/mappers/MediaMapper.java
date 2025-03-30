package com.xdpsx.onlineshop.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;
import com.xdpsx.onlineshop.entities.Media;

@Mapper
public interface MediaMapper {
    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    ViewMediaDTO toViewMediaDTO(Media media);
}
