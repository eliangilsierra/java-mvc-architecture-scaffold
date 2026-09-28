package com.eliangilsierra.mvcscaffold.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eliangilsierra.mvcscaffold.dto.response.CategoryResponseDto;
import com.eliangilsierra.mvcscaffold.dto.response.ProductResponseDto;
import com.eliangilsierra.mvcscaffold.service.IProductService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The web-layer slice of the test pyramid: verifies request validation and
 * response shape without a real service or database behind it — the service
 * is mocked, exactly as {@code CategoryProductIntegrationTest} exists to
 * prove the real wiring, and {@code ProductServiceImplTest} exists to prove
 * the business logic in isolation.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Test
    void createProduct_returnsCreatedWithBody() throws Exception {
        var category = new CategoryResponseDto(1L, "Electronics");
        when(productService.create(any())).thenReturn(
                new ProductResponseDto(1L, "Wireless Mouse", BigDecimal.valueOf(19.99), category));

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Wireless Mouse","price":19.99,"categoryId":1}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Electronics"));
    }

    @Test
    void createProduct_rejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","price":0,"categoryId":null}
                                """))
                .andExpect(status().isBadRequest());
    }
}
