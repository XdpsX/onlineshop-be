package com.xdpsx.onlineshop.dtos.common;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ModifyExclusiveDTO (
        @NotNull
        LocalDateTime lastRetrievedAt
){
}
