package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.repository.CategoryRepository;
import com.estudo.dscatalog.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


}
