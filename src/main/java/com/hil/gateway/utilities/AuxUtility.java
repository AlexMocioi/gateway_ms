package com.hil.gateway.utilities;

import com.hil.gateway.entity.dao.User;
import com.hil.gateway.jwt.JwtProvider;
import com.hil.gateway.repositories.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuxUtility {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    //Authenticate the user and retrun a jwt token
    public String authenticate(String username, String password) {
        Optional<User> userOptional =userRepository.findByUsername(username);
        if (!userOptional.isPresent()){
            throw new RuntimeException("no such user: "+username);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        userOptional.get().getRoles()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateJwtToken(authentication);
    }
    public ResponseEntity<String> isDataAvailable(String userName, String email){
        if (userRepository.existsByUsername(userName)) {
            return new ResponseEntity<String>("Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<String>("Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}

