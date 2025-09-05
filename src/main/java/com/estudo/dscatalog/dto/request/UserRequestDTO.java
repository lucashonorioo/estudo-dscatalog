package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.dto.response.RoleResponseDTO;
import com.estudo.dscatalog.model.User;
import com.estudo.dscatalog.service.validation.UserInsertValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;


public class UserRequestDTO {

    @NotBlank(message = "O primeiro nome não pode ser vazio")
    private String firstName;

    @NotBlank(message = "O segundo nome não pode ser vazio")
    private String lastName;

    @Email(message = "Inserir email valido")
    private String email;

    Set<RoleResponseDTO> roles = new HashSet<>();

    public UserRequestDTO(){

    }

    public UserRequestDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public UserRequestDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        user.getRoles().forEach( role -> this.roles.add(new RoleResponseDTO(role)));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleResponseDTO> getRoles() {
        return roles;
    }

}
