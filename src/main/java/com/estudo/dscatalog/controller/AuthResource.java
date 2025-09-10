package com.estudo.dscatalog.controller;

import com.estudo.dscatalog.dto.request.EmailRequestDTO;
import com.estudo.dscatalog.dto.request.NewPasswordRequestDTO;
import com.estudo.dscatalog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/recover-token")
    ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailRequestDTO emailRequestDTO){
        authService.createRecoverToken(emailRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/new-password")
    ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordRequestDTO newPasswordRequestDTO){
        authService.saveNewPassword(newPasswordRequestDTO);
        return ResponseEntity.noContent().build();
    }

}
