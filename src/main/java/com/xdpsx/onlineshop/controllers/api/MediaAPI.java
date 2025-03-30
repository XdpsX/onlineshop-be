package com.xdpsx.onlineshop.controllers.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.dtos.common.ErrorDetails;
import com.xdpsx.onlineshop.dtos.media.CreateMediaDTO;
import com.xdpsx.onlineshop.dtos.media.ViewMediaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Media API")
public interface MediaAPI {
    @Operation(summary = "Upload image", description = "Max file size: 2MB")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Created",
                content = @Content(schema = @Schema(implementation = ViewMediaDTO.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ErrorDetails.class)))
    })
    APIResponse<ViewMediaDTO> createMedia(@RequestParam String resource, @Valid @ModelAttribute CreateMediaDTO request);
}
