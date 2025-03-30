package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.media.CreateMediaDTO;
import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;

public interface MediaService {
    ViewMediaDTO createMedia(CreateMediaDTO request, MediaResourceType resourceType);
}
