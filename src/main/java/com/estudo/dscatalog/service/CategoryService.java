package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.CategoryRequestDTO;
import com.estudo.dscatalog.dto.response.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponseDTO findById(Long id);
    Page<CategoryResponseDTO> findAll(PageRequest pageRequest);
    CategoryResponseDTO insert(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO);
    void delete(Long id);
}
