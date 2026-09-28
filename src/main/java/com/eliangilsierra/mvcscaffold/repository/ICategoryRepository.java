package com.eliangilsierra.mvcscaffold.repository;

import com.eliangilsierra.mvcscaffold.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);
}
