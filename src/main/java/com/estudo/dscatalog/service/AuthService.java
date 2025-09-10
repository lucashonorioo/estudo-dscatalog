package com.estudo.dscatalog.service;

import com.estudo.dscatalog.dto.request.EmailRequestDTO;
import com.estudo.dscatalog.dto.request.NewPasswordRequestDTO;
import com.estudo.dscatalog.model.User;

public interface AuthService {

    void createRecoverToken(EmailRequestDTO emailRequestDTO);

    void saveNewPassword(NewPasswordRequestDTO newPasswordRequestDTO);
}
