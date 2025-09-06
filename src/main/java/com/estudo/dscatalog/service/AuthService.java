package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.EmailRequestDTO;

public interface AuthService {

    void createRecoverToken(EmailRequestDTO emailRequestDTO);
}
