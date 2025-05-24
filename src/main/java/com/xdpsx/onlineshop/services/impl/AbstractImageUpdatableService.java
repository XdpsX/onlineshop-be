package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.entities.common.HasImage;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.repositories.MediaRepository;

public abstract class AbstractImageUpdatableService {

    protected MediaRepository mediaRepository;

    public AbstractImageUpdatableService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    protected <T extends HasImage> void updateImage(T entity, String newImageId, MediaResourceType expectedType) {
        Media oldImage = entity.getImage();

        if (oldImage == null && newImageId == null) return;

        if (oldImage != null && !oldImage.getId().equals(newImageId)) {
            oldImage.setDeleteFlg(true);
            mediaRepository.save(oldImage);
            entity.setImage(null);
        }

        if (newImageId != null && (oldImage == null || !oldImage.getId().equals(newImageId))) {
            Media newImage = mediaRepository
                    .findPublicTempMediaById(newImageId, expectedType)
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, newImageId));

            newImage.setTempFlg(false);
            mediaRepository.save(newImage);

            entity.setImage(newImage);
        }
    }
}
