package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.exception.exceptions.DatabaseException;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;
import com.estudo.dscatalog.repository.CategoryRepository;
import com.estudo.dscatalog.repository.ProductRepository;
import com.estudo.dscatalog.service.impl.ProductServiceImpl;
import com.estudo.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;


    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 2L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        category = Factory.createCategory();

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);

        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);

        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());


        Mockito.doNothing().when(productRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);


    }

    @Test
    public void updateShouldThrowExceptionWhenIdNotExisting(){

        ProductRequestDTO productRequestDTO = Factory.createProductDTO();

        Assertions.assertThrows(ResourceNotFoundException.class, ()  ->{

            productService.update(nonExistingId, productRequestDTO);
        });

        Mockito.verify(productRepository).getReferenceById(nonExistingId);

    }


    @Test
    public void updateShouldReturnProductWhenIdExisting(){

        ProductRequestDTO productRequestDTO = Factory.createProductDTO();

        ProductResponseDTO result = productService.update(existingId, productRequestDTO);

        Assertions.assertNotNull(result);

        Assertions.assertEquals(product.getId(), result.getId());

        Mockito.verify(productRepository).getReferenceById(existingId);
        Mockito.verify(categoryRepository).getReferenceById(existingId);
        Mockito.verify(productRepository).save(product);

    }

    @Test
    public void findByIdShouldThrowExceptionIdNonExisting(){

        Assertions.assertThrows(ResourceNotFoundException.class, () ->{
            productService.findById(nonExistingId);
        });
        Mockito.verify(productRepository).findById(nonExistingId);

    }

    @Test
    public void findByIdShouldReturnProduct(){

        ProductResponseDTO result = productService.findById(existingId);
        Assertions.assertNotNull(result);

        Assertions.assertEquals(product.getId(), result.getId());


        Mockito.verify(productRepository).findById(existingId);
    }


    @Test
    public void findAllPagedShouldReturnPage(){

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductResponseDTO> result = productService.findAll(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(productRepository).findAll(pageable);
    }


    @Test
    public void shouldThrowExceptionWhenDependentId(){

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });

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
