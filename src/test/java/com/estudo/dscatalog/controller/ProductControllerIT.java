package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.Product;
import com.estudo.dscatalog.repository.ProductRepository;
import com.estudo.dscatalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductControllerIT {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;


    @BeforeEach
    void setUp() throws Exception{

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

    }


    @Test
    public void deleteShouldThrowExceptionWhenIdDoesNotExisting(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExisting(){

        productService.delete(existingId);

        Assertions.assertEquals(countTotalProducts - 1, productRepository.count());
    }

    @Test
    public void findAllPageShouldReturnPageWhenPage0Size10(){

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductResponseDTO> result = productService.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    public void findAllPageShouldReturnEmptyPageWhenPageDoesNotExist(){

        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductResponseDTO> result = productService.findAll(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPageShouldReturnSortedPageWhenSortByName(){

        PageRequest pageRequest = PageRequest.of(0 , 10, Sort.by("name"));

        Page<ProductResponseDTO> result = productService.findAll(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }

}
