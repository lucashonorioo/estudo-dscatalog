package com.estudo.dscatalog.repository;

import com.estudo.dscatalog.model.Product;
import com.estudo.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;


    private long existingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 10L;
        countTotalProducts = 25L;

    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExist(){


        productRepository.deleteById(existingId);

        Optional<Product> product = productRepository.findById(existingId);

        Assertions.assertFalse(product.isPresent());

    }

    @Test
    public void saveShouldPersistWhiAutoincrementWhenIdIsNull(){

        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void shouldReturnOptionalNotNullWhenIdExist(){

        Optional<Product> product = productRepository.findById(existingId);

        Assertions.assertTrue(product.isPresent());
    }


}
