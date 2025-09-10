package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.config.SecurityCommonsConfig;
import com.estudo.dscatalog.dto.request.EmailRequestDTO;
import com.estudo.dscatalog.dto.request.NewPasswordRequestDTO;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.PasswordRecover;
import com.estudo.dscatalog.model.User;
import com.estudo.dscatalog.repository.PasswordRecoverRepository;
import com.estudo.dscatalog.repository.UserRepository;
import com.estudo.dscatalog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PasswordRecoverRepository passwordRecoverRepository;

    private final EmailServiceImpl emailService;

    public AuthServiceImpl(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordRecoverRepository = passwordRecoverRepository;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public void createRecoverToken(EmailRequestDTO emailRequestDTO) {
        User user = userRepository.findByEmail(emailRequestDTO.getEmail());
        if(user == null){
            throw new ResourceNotFoundException("Email não encontrado");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setEmail(emailRequestDTO.getEmail());
        passwordRecover.setToken(token);
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        passwordRecover = passwordRecoverRepository.save(passwordRecover);

        String text = "Acesse o link para definir uma nova senha\n\n" + recoverUri + token;

        emailService.sendEmail(emailRequestDTO.getEmail(), "Recuperação de senha", text);
        
    }

    @Transactional
    @Override
    public void saveNewPassword(NewPasswordRequestDTO newPasswordRequestDTO) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(newPasswordRequestDTO.getToken(), Instant.now());
        if(result.size() == 0){
            throw new ResourceNotFoundException("Token inválido");
        }

        User user = userRepository.findByEmail(result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode(newPasswordRequestDTO.getPassword()));
        user = userRepository.save(user);

    }
}
