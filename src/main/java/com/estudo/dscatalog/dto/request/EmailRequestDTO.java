package com.estudo.dscatalog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequestDTO {

    @NotBlank(message = "Campo obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    public EmailRequestDTO(){

    }

    public EmailRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
