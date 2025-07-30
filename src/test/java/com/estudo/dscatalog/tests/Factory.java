package com.estudo.dscatalog.tests;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone" ,800.0 , "https://img.com/img.png");
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductRequestDTO createProductDTO(){
        Product product = createProduct();
        return new ProductRequestDTO(product, product.getCategories());
    }

}
