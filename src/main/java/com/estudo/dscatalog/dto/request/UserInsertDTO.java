package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.dto.response.RoleResponseDTO;
import com.estudo.dscatalog.model.User;
import com.estudo.dscatalog.service.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@UserInsertValid
public class UserInsertDTO extends UserRequestDTO{

    @NotBlank(message = "A senha não pode ser vazio")
    private String password;

    public UserInsertDTO(){
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
