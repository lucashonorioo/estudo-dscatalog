package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.UserRequestDTO;
import com.estudo.dscatalog.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO findById(Long id);
    Page<UserResponseDTO> findAll(Pageable pageable);
    UserResponseDTO insert(UserRequestDTO userRequestDTO);
    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);
    void delete(Long id);

}
