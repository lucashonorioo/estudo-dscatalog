package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.UserInsertDTO;
import com.estudo.dscatalog.dto.request.UserUpdateDTO;
import com.estudo.dscatalog.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO findById(Long id);
    UserResponseDTO findMe();
    Page<UserResponseDTO> findAll(Pageable pageable);
    UserResponseDTO insert(UserInsertDTO userRequestDTO);
    UserResponseDTO update(Long id, UserUpdateDTO userRequestDTO);
    void delete(Long id);

}
