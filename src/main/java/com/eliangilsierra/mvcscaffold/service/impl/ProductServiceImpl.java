package com.eliangilsierra.mvcscaffold.service.impl;

import com.eliangilsierra.mvcscaffold.dto.request.ProductRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.ProductResponseDto;
import com.eliangilsierra.mvcscaffold.entity.Category;
import com.eliangilsierra.mvcscaffold.entity.Product;
import com.eliangilsierra.mvcscaffold.exception.ResourceNotFoundException;
import com.eliangilsierra.mvcscaffold.mapper.IProductMapper;
import com.eliangilsierra.mvcscaffold.repository.ICategoryRepository;
import com.eliangilsierra.mvcscaffold.repository.IProductRepository;
import com.eliangilsierra.mvcscaffold.service.IProductService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IProductMapper productMapper;

    public ProductServiceImpl(
            IProductRepository productRepository,
            ICategoryRepository categoryRepository,
            IProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDto create(ProductRequestDto request) {
        Product product = productMapper.toEntity(request);
        product.setCategory(findCategoryOrThrow(request.categoryId()));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    public ProductResponseDto getById(Long id) {
        return productMapper.toResponseDto(findEntityOrThrow(id));
    }

    @Override
    public List<ProductResponseDto> getAll(Long categoryId) {
        List<Product> products = categoryId != null
                ? productRepository.findByCategoryId(categoryId)
                : productRepository.findAll();
        return products.stream().map(productMapper::toResponseDto).toList();
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto request) {
        Product product = findEntityOrThrow(id);
        product.setName(request.name());
        product.setPrice(request.price());
        product.setCategory(findCategoryOrThrow(request.categoryId()));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = findEntityOrThrow(id);
        productRepository.delete(product);
    }

    private Product findEntityOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
    }
}
