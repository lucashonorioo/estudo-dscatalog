package com.estudo.dscatalog.repository;

import com.estudo.dscatalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
