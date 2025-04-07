package com.xdpsx.onlineshop.dtos.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.xdpsx.onlineshop.entities.Category;

public record CategoryTreeFilter(@Min(1) @Max(Category.MAX_DEPTH) Integer maxLevel, String sort) {}
