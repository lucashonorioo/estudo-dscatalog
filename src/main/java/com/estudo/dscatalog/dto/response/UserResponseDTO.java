package com.estudo.dscatalog.dto.response;

import com.estudo.dscatalog.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<RoleResponseDTO> roles = new HashSet<>();

    public UserResponseDTO(){

    }

    public UserResponseDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UserResponseDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        user.getRoles().forEach( role -> this.roles.add(new RoleResponseDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<RoleResponseDTO> getRoles() {
        return roles;
    }
}
