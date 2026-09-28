package com.eliangilsierra.mvcscaffold.service;

import com.eliangilsierra.mvcscaffold.dto.request.CategoryRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.CategoryResponseDto;
import java.util.List;

public interface ICategoryService {

    CategoryResponseDto create(CategoryRequestDto request);

    CategoryResponseDto getById(Long id);

    List<CategoryResponseDto> getAll();
}
