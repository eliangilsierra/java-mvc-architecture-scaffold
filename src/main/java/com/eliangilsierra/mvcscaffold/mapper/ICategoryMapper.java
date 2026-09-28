package com.eliangilsierra.mvcscaffold.mapper;

import com.eliangilsierra.mvcscaffold.dto.request.CategoryRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.CategoryResponseDto;
import com.eliangilsierra.mvcscaffold.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryRequestDto dto);

    CategoryResponseDto toResponseDto(Category entity);
}
