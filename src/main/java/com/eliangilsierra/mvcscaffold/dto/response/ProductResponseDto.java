package com.eliangilsierra.mvcscaffold.dto.response;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        BigDecimal price,
        CategoryResponseDto category
) {
}
