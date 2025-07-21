package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {

    ProductResponseDTO findById(Long id);
    Page<ProductResponseDTO> findAll(PageRequest pageRequest);
    ProductResponseDTO insert(ProductRequestDTO productRequestDTO);
    ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO);
    void delete(Long id);
}
