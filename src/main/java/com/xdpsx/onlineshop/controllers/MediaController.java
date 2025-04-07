package com.xdpsx.onlineshop.controllers;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.constants.messages.SMessage;
import com.xdpsx.onlineshop.controllers.docs.MediaAPI;
import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.dtos.media.CreateMediaDTO;
import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.services.MediaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MediaController implements MediaAPI {
    private final MediaService mediaService;

    @PostMapping(path = "/media/image-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ViewMediaDTO> createMedia(
            @RequestParam String resource, @Valid @ModelAttribute CreateMediaDTO request) {
        MediaResourceType resourceType = MediaResourceType.fromResource(resource);
        ViewMediaDTO data = mediaService.createMedia(request, resourceType);
        return new APIResponse<>(HttpStatus.CREATED, data, SMessage.CREATE_SUCCESSFULLY);
    }
}
