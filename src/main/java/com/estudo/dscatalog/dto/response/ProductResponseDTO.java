package com.estudo.dscatalog.dto.response;


import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;

import java.util.HashSet;
import java.util.Set;

public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    private Set<CategoryResponseDTO> categories = new HashSet<>();

    public ProductResponseDTO(){

    }

    public ProductResponseDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductResponseDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        for(Category category : product.getCategories()){
            categories.add(new CategoryResponseDTO(category));
        }

    }

    public Long getId() {
        return id;
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

    public Set<CategoryResponseDTO> getCategories() {
        return categories;
    }
}
