package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.service.impl.ProductServiceImpl;
import com.estudo.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductServiceImpl productService;

    private Long existingId;
    private Long nonExistingId;
    private ProductResponseDTO productResponseDTO;
    private PageImpl<ProductResponseDTO> productResponseDTOPage;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 1L;
        nonExistingId = 2L;
        productResponseDTO = Factory.createProductResponseDTO();
        productResponseDTOPage = new PageImpl<>(List.of(productResponseDTO));

        when(productService.findAll(any())).thenReturn(productResponseDTOPage);
        when(productService.findById(existingId)).thenReturn(productResponseDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

    }

    @Test
    public void findShouldReturnPage() throws Exception{

        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    public void findIdShouldReturnProductWhenIdExistent() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void findIdShouldThrowNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());

    }

}
