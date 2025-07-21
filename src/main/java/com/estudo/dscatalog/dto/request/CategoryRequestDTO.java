package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.model.Category;
import jakarta.validation.constraints.NotBlank;

public class CategoryRequestDTO {

    private Long id;

    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

    public CategoryRequestDTO(){

    }

    public CategoryRequestDTO(String name) {
        this.name = name;
    }

    public CategoryRequestDTO(Category category) {
        name = category.getName();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
