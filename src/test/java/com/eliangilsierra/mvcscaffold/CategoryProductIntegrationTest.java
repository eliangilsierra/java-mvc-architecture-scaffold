package com.eliangilsierra.mvcscaffold;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliangilsierra.mvcscaffold.dto.request.CategoryRequestDto;
import com.eliangilsierra.mvcscaffold.dto.request.ProductRequestDto;
import com.eliangilsierra.mvcscaffold.dto.response.CategoryResponseDto;
import com.eliangilsierra.mvcscaffold.dto.response.ProductResponseDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Exercises the real, wired-up application (H2 swapped for a real Postgres
 * container — see ADR-0003) end to end through its HTTP API: create a
 * category, create a product under it, fetch it back, and confirm a missing
 * product 404s with the new {@link org.springframework.http.ProblemDetail}
 * shape (ADR-0002).
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryProductIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void createsCategoryAndProductThenRetrievesThem() {
        ResponseEntity<CategoryResponseDto> categoryResponse = restTemplate.postForEntity(
                url("/categories"), new CategoryRequestDto("Electronics"), CategoryResponseDto.class);
        assertThat(categoryResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long categoryId = categoryResponse.getBody().id();

        ResponseEntity<ProductResponseDto> productResponse = restTemplate.postForEntity(
                url("/products"),
                new ProductRequestDto("Wireless Mouse", BigDecimal.valueOf(19.99), categoryId),
                ProductResponseDto.class);
        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long productId = productResponse.getBody().id();

        ResponseEntity<ProductResponseDto> getResponse =
                restTemplate.getForEntity(url("/products/" + productId), ProductResponseDto.class);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().name()).isEqualTo("Wireless Mouse");
        assertThat(getResponse.getBody().category().name()).isEqualTo("Electronics");
    }

    @Test
    void missingProductReturnsProblemDetail() {
        ResponseEntity<String> response = restTemplate.getForEntity(url("/products/999999"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("\"status\":404");
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
