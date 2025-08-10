package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.request.ProductRequestDTO;
import com.estudo.dscatalog.dto.response.ProductResponseDTO;
import com.estudo.dscatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable Long id){
        ProductResponseDTO productResponseDTO = productService.findById(id);
        return ResponseEntity.ok().body(productResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> findAll(Pageable pageable)
    {
        Page<ProductResponseDTO> productResponseDTOPage = productService.findAll(pageable);
        return ResponseEntity.ok().body(productResponseDTOPage);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> insert(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO productResponseDTO = productService.insert(productRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(productResponseDTO.getId()).toUri();
        return ResponseEntity.created(location).body(productResponseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO productResponseDTO = productService.update(id, productRequestDTO);
        return ResponseEntity.ok().body(productResponseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
