package com.eliangilsierra.mvcscaffold.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.eliangilsierra.mvcscaffold.entity.Category;
import com.eliangilsierra.mvcscaffold.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * The other end of the test pyramid from {@code ProductServiceImplTest}: this
 * slice actually talks to a database (H2), proving the custom query method
 * works — something a mocked-repository unit test can't tell you.
 */
@DataJpaTest
class IProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IProductRepository productRepository;

    @Test
    void findByCategoryIdReturnsOnlyProductsInThatCategory() {
        Category category = entityManager.persistFlushFind(Category.builder().name("Test Category").build());
        Product product = entityManager.persistFlushFind(
                Product.builder().name("Test Product").price(BigDecimal.TEN).category(category).build());

        List<Product> found = productRepository.findByCategoryId(category.getId());

        assertThat(found).extracting(Product::getId).containsExactly(product.getId());
    }

    @Test
    void findByCategoryIdReturnsEmptyForUnknownCategory() {
        assertThat(productRepository.findByCategoryId(999_999L)).isEmpty();
    }
}
