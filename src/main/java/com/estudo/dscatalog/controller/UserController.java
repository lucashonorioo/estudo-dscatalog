package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.request.UserRequestDTO;
import com.estudo.dscatalog.dto.response.UserResponseDTO;
import com.estudo.dscatalog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        UserResponseDTO userResponseDTO = userService.findById(id);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @GetMapping
    ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable){
        Page<UserResponseDTO> userResponseDTO = userService.findAll(pageable);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PostMapping
    ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.insert(userRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userResponseDTO.getId()).toUri();
        return ResponseEntity.created(location).body(userResponseDTO);
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO){
        UserResponseDTO userResponseDTO = userService.update(id, userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
