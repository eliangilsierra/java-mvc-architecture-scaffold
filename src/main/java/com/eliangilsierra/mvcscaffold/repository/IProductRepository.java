package com.eliangilsierra.mvcscaffold.repository;

import com.eliangilsierra.mvcscaffold.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);
}
