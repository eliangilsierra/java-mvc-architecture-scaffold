package com.eliangilsierra.mvcscaffold.service.impl;

import com.eliangilsierra.mvcscaffold.dto.request.CategoryRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.CategoryResponseDto;
import com.eliangilsierra.mvcscaffold.entity.Category;
import com.eliangilsierra.mvcscaffold.exception.DuplicateResourceException;
import com.eliangilsierra.mvcscaffold.exception.ResourceNotFoundException;
import com.eliangilsierra.mvcscaffold.mapper.ICategoryMapper;
import com.eliangilsierra.mvcscaffold.repository.ICategoryRepository;
import com.eliangilsierra.mvcscaffold.service.ICategoryService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    public CategoryServiceImpl(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Category already exists: " + request.name());
        }
        Category saved = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toResponseDto(saved);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toResponseDto(findEntityOrThrow(id));
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponseDto).toList();
    }

    private Category findEntityOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }
}
