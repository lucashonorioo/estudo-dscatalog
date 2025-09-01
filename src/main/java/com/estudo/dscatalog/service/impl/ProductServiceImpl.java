package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.dto.request.CategoryRequestDTO;
import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.CategoryResponseDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.exception.exceptions.DatabaseException;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;
import com.estudo.dscatalog.projections.ProductProjection;
import com.estudo.dscatalog.repository.CategoryRepository;
import com.estudo.dscatalog.repository.ProductRepository;
import com.estudo.dscatalog.service.ProductService;
import com.estudo.dscatalog.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService  {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id deve ser positivo e não nulo");
        }
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> findAll(String name, String categoryId, Pageable pageable) {

        List<Long> categoryIds = Arrays.asList();
        if(!"0".equals(categoryId)){
            categoryIds = Arrays.asList(categoryId.split(",")).stream().map(Long::parseLong).toList();
        }
        Page<ProductProjection> page = productRepository.searchProducts(categoryIds, name.trim(), pageable);
        List<Long> productIds = page.map(ProductProjection::getId).stream().toList();

        List<Product> entities = productRepository.searchProductsWithCategories(productIds);

        entities = (List<Product>) Utils.replace(page.getContent(), entities);

        List<ProductResponseDTO> productResponseDTOS = entities.stream().map( e -> new ProductResponseDTO(e, e.getCategories())).toList();

        Page<ProductResponseDTO> productResponseDTOPage = new PageImpl<>(productResponseDTOS, page.getPageable(), page.getTotalPages());
        return productResponseDTOPage;
    }

    @Override
    @Transactional
    public ProductResponseDTO insert(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        toDto(productRequestDTO, product);
        product = productRepository.save(product);
        return new ProductResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id deve ser positivo e não nulo");
        }
        try{
            Product product = productRepository.getReferenceById(id);
            toDto(productRequestDTO, product);
            product = productRepository.save(product);
            return new ProductResponseDTO(product);
        }
        catch (EntityNotFoundException e){
             throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(id == null || id <= 0){
            throw new ResourceNotFoundException("O id deve ser positivo e não nulo");
        }
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encotrado");
        }
        try{
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

    private void toDto(ProductRequestDTO productRequestDTO, Product product){
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setImgUrl(productRequestDTO.getImgUrl());
        product.setDate(productRequestDTO.getDate());

        product.getCategories().clear();

        for(CategoryRequestDTO categoryRequestDTO : productRequestDTO.getCategories()){
            Category category = categoryRepository.getReferenceById(categoryRequestDTO.getId());
            product.getCategories().add(category);
        }
    }

}
