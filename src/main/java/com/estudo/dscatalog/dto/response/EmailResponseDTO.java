package com.estudo.dscatalog.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailResponseDTO {

    private String email;

    public EmailResponseDTO(){

    }

    public EmailResponseDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
