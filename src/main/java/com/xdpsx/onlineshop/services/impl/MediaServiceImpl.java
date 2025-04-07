package com.xdpsx.onlineshop.services.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.dtos.media.CloudinaryUploadResponse;
import com.xdpsx.onlineshop.dtos.media.CreateMediaDTO;
import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.mappers.MediaMapper;
import com.xdpsx.onlineshop.repositories.MediaRepository;
import com.xdpsx.onlineshop.services.MediaService;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final CloudinaryUploader cloudinaryUploader;

    @Override
    public ViewMediaDTO createMedia(CreateMediaDTO request, MediaResourceType resourceType) {
        validateImageSize(request.file(), resourceType);
        CloudinaryUploadResponse response =
                cloudinaryUploader.uploadFile(request.file(), resourceType.getUploadOptions());
        Media media = Media.builder()
                .id(response.displayName())
                .externalId(response.publicId())
                .url(response.url())
                .caption(request.caption())
                .contentType(request.file().getContentType())
                .resourceType(resourceType)
                .tempFlg(true)
                .deleteFlg(false)
                .build();
        Media savedMedia = mediaRepository.save(media);
        return MediaMapper.INSTANCE.toViewMediaDTO(savedMedia);
    }

    public void validateImageSize(MultipartFile file, MediaResourceType resourceType) {
        if (resourceType.minWidth() == null) {
            return;
        }
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("Invalid image format");
            }

            int width = image.getWidth();
            if (width < resourceType.minWidth()) {
                throw new BadRequestException(EMessage.INVALID_IMAGE_WIDTH, resourceType.minWidth());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        }
    }
}
