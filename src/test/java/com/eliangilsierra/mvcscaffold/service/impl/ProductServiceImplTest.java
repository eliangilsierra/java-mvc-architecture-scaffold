package com.eliangilsierra.mvcscaffold.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eliangilsierra.mvcscaffold.dto.request.ProductRequestDto;
import com.eliangilsierra.mvcscaffold.entity.Category;
import com.eliangilsierra.mvcscaffold.entity.Product;
import com.eliangilsierra.mvcscaffold.exception.ResourceNotFoundException;
import com.eliangilsierra.mvcscaffold.mapper.ICategoryMapperImpl;
import com.eliangilsierra.mvcscaffold.mapper.IProductMapper;
import com.eliangilsierra.mvcscaffold.mapper.IProductMapperImpl;
import com.eliangilsierra.mvcscaffold.repository.ICategoryRepository;
import com.eliangilsierra.mvcscaffold.repository.IProductRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * A layered service is easy to unit test in isolation: mock the repositories,
 * use the real (generated) mapper, and verify behavior without a Spring context
 * or a database.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private ICategoryRepository categoryRepository;

    private final IProductMapper productMapper = new IProductMapperImpl(new ICategoryMapperImpl());

    private ProductServiceImpl productService;

    @Test
    void createsProductWhenCategoryExists() {
        productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);
        Category category = Category.builder().id(1L).name("Electronics").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(org.mockito.ArgumentMatchers.any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    product.setId(10L);
                    return product;
                });

        var response = productService.create(new ProductRequestDto("Mouse", BigDecimal.valueOf(19.99), 1L));

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.category().name()).isEqualTo("Electronics");
        verify(productRepository).save(org.mockito.ArgumentMatchers.any(Product.class));
    }

    @Test
    void throwsWhenCategoryDoesNotExist() {
        productService = new ProductServiceImpl(productRepository, categoryRepository, productMapper);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                productService.create(new ProductRequestDto("Mouse", BigDecimal.valueOf(19.99), 99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
