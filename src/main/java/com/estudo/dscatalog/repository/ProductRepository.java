package com.estudo.dscatalog.repository;

import com.estudo.dscatalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
