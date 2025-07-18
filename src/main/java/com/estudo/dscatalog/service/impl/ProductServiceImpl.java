package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductService productService;

    public ProductServiceImpl(ProductService productService) {
        this.productService = productService;
    }


}
