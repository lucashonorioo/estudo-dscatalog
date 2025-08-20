package com.estudo.dscatalog.dto.request;

import com.estudo.dscatalog.dto.response.RoleResponseDTO;
import com.estudo.dscatalog.model.User;
import com.estudo.dscatalog.service.validation.UserInsertValid;
import com.estudo.dscatalog.service.validation.UserUpdateValid;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;


@UserUpdateValid
public class UserUpdateDTO extends UserRequestDTO{

}
