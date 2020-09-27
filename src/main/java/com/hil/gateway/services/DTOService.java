package com.hil.gateway.services;

import com.hil.gateway.entity.dao.User;
import com.hil.gateway.entity.dto.AuthenticatedUserDTO;
import com.hil.gateway.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DTOService {

    @Autowired
    UserRepository userRepository;

    public AuthenticatedUserDTO create(String jwt, String username) {
        AuthenticatedUserDTO authenticatedUserDTO = new AuthenticatedUserDTO();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(!userOptional.isPresent()){
            throw new RuntimeException();
        }

        User user=userOptional.get();
        authenticatedUserDTO.setName(user.getName());
        authenticatedUserDTO.setUsername(user.getUsername());
        authenticatedUserDTO.setEmail(user.getEmail());
        authenticatedUserDTO.setRoles(user.getRoles());
        authenticatedUserDTO.setToken(jwt);

        return authenticatedUserDTO;
    }
}
