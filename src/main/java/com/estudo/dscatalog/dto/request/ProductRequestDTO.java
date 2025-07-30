package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.model.Category;
import com.estudo.dscatalog.model.Product;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private Instant date;

    @NotEmpty(message = "A categoria não pode ser vazia")
    private List<CategoryRequestDTO> categories = new ArrayList<>();


    public ProductRequestDTO(){

    }

    public ProductRequestDTO(String name, String description, Double price, String imgUrl, Instant date) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductRequestDTO(Product product) {
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        date = product.getDate();
    }

    public ProductRequestDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach( c -> this.categories.add(new CategoryRequestDTO(c)));
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

    public Instant getDate() {
        return date;
    }

    public List<CategoryRequestDTO> getCategories() {
        return categories;
    }
}
