package com.xdpsx.onlineshop.dtos.common;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ModifyExclusiveDTO(@NotNull LocalDateTime lastRetrievedAt) {}
