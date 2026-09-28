package com.eliangilsierra.mvcscaffold.service;

import com.eliangilsierra.mvcscaffold.dto.request.ProductRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.ProductResponseDto;
import java.util.List;

public interface IProductService {

    ProductResponseDto create(ProductRequestDto request);

    ProductResponseDto getById(Long id);

    List<ProductResponseDto> getAll(Long categoryId);

    ProductResponseDto update(Long id, ProductRequestDto request);

    void delete(Long id);
}
