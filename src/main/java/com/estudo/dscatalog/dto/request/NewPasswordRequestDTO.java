package com.estudo.dscatalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordRequestDTO {

    @NotBlank(message = "Campo obrigatorio")
    private String token;

    @NotBlank(message = "Campo obrigatorio")
    @Size(min = 8, message = "Deve ter no minimo 8 catacteres")
    private String password;

    public NewPasswordRequestDTO(){

    }

    public NewPasswordRequestDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
