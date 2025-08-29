package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.CategoryRequestDTO;
import com.estudo.dscatalog.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO findById(Long id);
    List<CategoryResponseDTO> findAll();
    CategoryResponseDTO insert(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO);
    void delete(Long id);
}
