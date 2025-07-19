package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.Set;

public class ProductRequestDTO {

    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

    @NotBlank(message = "A descrição não pode ser vazio")
    private String description;

    @NotNull(message = "O preço não pode ser vazio")
    @Positive(message = "O preço não pode ser negativo")
    private Double price;

    private String imgUrl;

    @NotEmpty(message = "A categoria não pode ser vazia")
    private Set<CategoryRequestDTO> categories = new HashSet<>();

    public ProductRequestDTO(){

    }

    public ProductRequestDTO(String name, String description, Double price, String imgUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductRequestDTO(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgUrl = product.getImgUrl();
        for(Category category : product.getCategories()){
            categories.add(new CategoryRequestDTO(category));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Set<CategoryRequestDTO> getCategories() {
        return categories;
    }
}
