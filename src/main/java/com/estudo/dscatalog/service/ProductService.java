package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponseDTO findById(Long id);
    Page<ProductResponseDTO> findAll(String name, String categoryId, Pageable pageablege);
    ProductResponseDTO insert(ProductRequestDTO productRequestDTO);
    ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO);
    void delete(Long id);
}
