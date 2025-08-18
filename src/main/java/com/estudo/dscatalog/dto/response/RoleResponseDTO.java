package com.estudo.dscatalog.dto.response;

import com.estudo.dscatalog.model.Role;

public class RoleResponseDTO {

    private Long id;
    private String authority;

    public RoleResponseDTO(){

    }

    public RoleResponseDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleResponseDTO(Role role) {
        id = role.getId();
        authority = role.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public String getAuthority() {
        return authority;
    }
}
