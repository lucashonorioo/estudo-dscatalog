package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.dto.request.CategoryRequestDTO;
import com.estudo.dscatalog.dto.response.CategoryResponseDTO;
import com.estudo.dscatalog.exception.exceptions.DatabaseException;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.repository.CategoryRepository;
import com.estudo.dscatalog.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id precisa ser positivo e não nulo");
        }
        Category category =  categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new CategoryResponseDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findAll(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map( c -> new CategoryResponseDTO(c));
    }

    @Override
    @Transactional
    public CategoryResponseDTO insert(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        toDTO(categoryRequestDTO, category);
        category = categoryRepository.save(category);
        return new CategoryResponseDTO(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id precisa ser positivo e não nulo");
        }
        try{
            Category category = categoryRepository.getReferenceById(id);
            toDTO(categoryRequestDTO, category);
            category = categoryRepository.save(category);
            return new CategoryResponseDTO(category);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id precisa ser positivo e não nulo");
        }
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encotrado");
        }
        try {
            categoryRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    public static void toDTO(CategoryRequestDTO categoryRequestDTO, Category category){
                category.setName(categoryRequestDTO.getName());
    }


}
