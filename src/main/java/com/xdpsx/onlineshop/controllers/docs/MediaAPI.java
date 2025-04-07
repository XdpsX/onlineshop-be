package com.xdpsx.onlineshop.controllers.docs;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.dtos.common.ErrorDTO;
import com.xdpsx.onlineshop.dtos.common.ErrorDetailsDTO;
import com.xdpsx.onlineshop.dtos.media.CreateMediaDTO;
import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Media API")
public interface MediaAPI {

    @Operation(
            summary = "Upload image",
            description = "Uploads an image file to the server. Max file size: 2MB",
            requestBody =
                    @RequestBody(
                            description = "containing the image file and its metadata",
                            required = true,
                            content =
                                    @Content(
                                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                            schema = @Schema(implementation = CreateMediaDTO.class))),
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content = @Content(schema = @Schema(implementation = CreateMediaVM.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Validation error / Invalid image width",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(oneOf = {ErrorDTO.class, ErrorDetailsDTO.class}))),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
            })
    APIResponse<ViewMediaDTO> createMedia(
            @Parameter(description = "category, brand,...") @RequestParam String resource,
            @Valid @ModelAttribute CreateMediaDTO request);

    class CreateMediaVM extends APIResponse<ViewMediaDTO> {}
}
