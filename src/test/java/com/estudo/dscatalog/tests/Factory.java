package com.estudo.dscatalog.tests;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;

import java.time.Instant;
import java.util.HashSet;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone" ,800.0 , "https://img.com/img.png");
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductRequestDTO createProductDTO(){
        Product product = createProduct();
        return new ProductRequestDTO(product, product.getCategories());
    }

    public static ProductResponseDTO createProductResponseDTO(){
        Product product = createProduct();
        return new ProductResponseDTO(product);
    }

    public static Category createCategory(){
        return new Category(1L, "Electronics");
    }




}
