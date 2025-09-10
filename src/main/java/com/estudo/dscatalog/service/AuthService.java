package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.EmailRequestDTO;
import com.estudo.dscatalog.dto.request.NewPasswordRequestDTO;

public interface AuthService {

    void createRecoverToken(EmailRequestDTO emailRequestDTO);

    void saveNewPassword(NewPasswordRequestDTO newPasswordRequestDTO);
}
