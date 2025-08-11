package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.exception.exceptions.DatabaseException;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.service.impl.ProductServiceImpl;
import com.estudo.dscatalog.tests.Factory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private ProductResponseDTO productResponseDTO;
    private PageImpl<ProductResponseDTO> productResponseDTOPage;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() throws Exception{

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        productResponseDTO = Factory.createProductResponseDTO();
        productRequestDTO = Factory.createProductDTO();
        productResponseDTOPage = new PageImpl<>(List.of(productResponseDTO));

        when(productService.findAll(any())).thenReturn(productResponseDTOPage);

        when(productService.findById(existingId)).thenReturn(productResponseDTO);
        when(productService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(productService.update(eq(existingId), any())).thenReturn(productResponseDTO);
        when(productService.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

        when(productService.insert(any())).thenReturn(productResponseDTO);

        doNothing().when(productService).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DatabaseException.class).when(productService).delete(dependentId);

    }

    @Test
    public void deleteDoesNotSuccessShouldThrowExceptionWhenIdIsDependent() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteDoesNoSuccessShouldThrowExceptionWhenIdNotExist() throws Exception {

        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }


    @Test
    public void deleteSuccessShouldReturnDoNothing() throws Exception {


        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON));


        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }


    @Test
    public void updateShouldReturnProductDTOWhenIdExistent() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}",existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldThrowNotFoundWhenIdDoesNotExist() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(productRequestDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
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
