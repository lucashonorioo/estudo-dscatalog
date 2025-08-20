package com.estudo.dscatalog.service.impl;

import com.estudo.dscatalog.dto.request.UserInsertDTO;
import com.estudo.dscatalog.dto.request.UserRequestDTO;
import com.estudo.dscatalog.dto.request.UserUpdateDTO;
import com.estudo.dscatalog.dto.response.RoleResponseDTO;
import com.estudo.dscatalog.dto.response.UserResponseDTO;
import com.estudo.dscatalog.exception.exceptions.DatabaseException;
import com.estudo.dscatalog.exception.exceptions.ResourceNotFoundException;
import com.estudo.dscatalog.model.Role;
import com.estudo.dscatalog.model.User;
import com.estudo.dscatalog.projections.UserDetailsProjection;
import com.estudo.dscatalog.repository.RoleRepository;
import com.estudo.dscatalog.repository.UserRepository;
import com.estudo.dscatalog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO findById(Long id) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id deve ser positivo e não nulo");
        }
        User user = userRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponseDTO::new);
    }

    @Transactional
    @Override
    public UserResponseDTO insert(UserInsertDTO userInsertDTO) {
        User user = new User();
        toDto(userInsertDTO, user);
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    @Override
    public UserResponseDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id deve ser positivo e não nulo");
        }
        try {
            User user = userRepository.getReferenceById(id);
            toDto(userUpdateDTO, user);
            return new UserResponseDTO(user);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if(id == null || id <= 0){
            throw new RuntimeException("O id deve ser positivo e não nulo");
        }
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encotrado");
        }
        try {
            userRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e){
            throw new DatabaseException("Recurso possui dependencia com o sistema");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if(result.size() == 0){
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for(UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    private void toDto(UserRequestDTO userRequestDTO, User user){
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setEmail(userRequestDTO.getEmail());
        for(RoleResponseDTO roleResponseDTO : userRequestDTO.getRoles()){
            Role role = roleRepository.getReferenceById(roleResponseDTO.getId());
            user.getRoles().add(role);
        }
    }


}
