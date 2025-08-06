package com.estudo.dscatalog.service;

import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.repository.ProductRepository;
import com.estudo.dscatalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;


    private long existingId;
    private long nonExistingId;
    private long dependentId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;

        Mockito.doNothing().when(productRepository).deleteById(existingId);

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);


    }

    @Test
    public void shouldThrowExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           productService.delete(nonExistingId);
        });
    }

    @Test
    public void shouldDeleteSuccessfullyWhenIdExists(){

        Assertions.assertDoesNotThrow( () -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository).deleteById(existingId);
    }

}
