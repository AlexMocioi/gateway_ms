package com.hil.gateway.controllers;


import com.hil.gateway.entity.dao.User;
import com.hil.gateway.entity.dto.AuthenticatedUserDTO;
import com.hil.gateway.jwt.message.request.LoginForm;
import com.hil.gateway.jwt.message.request.SignUpForm;
import com.hil.gateway.repositories.UserRepository;
import com.hil.gateway.services.DTOService;
import com.hil.gateway.utilities.AuxUtility;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    private AuxUtility auxUtility;

    private DTOService dtoService;

    private UserRepository userRepository;

    private PasswordEncoder encoder;


    @Autowired
    public AuthentificationController(AuxUtility auxUtility, DTOService dtoService, UserRepository userRepository, PasswordEncoder encoder) {
        this.auxUtility = auxUtility;
        this.dtoService = dtoService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        String jwt = auxUtility.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        AuthenticatedUserDTO authenticatedUserDTO = dtoService.create(jwt, loginRequest.getUsername());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(authenticatedUserDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        ResponseEntity<String> isDataAvailable = auxUtility.isDataAvailable(signUpRequest.getUsername(), signUpRequest.getEmail());
        if (isDataAvailable != null) {
            return isDataAvailable;
        }
        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));


        user.setRoles(signUpRequest.getRoles());
        user = userRepository.save(user);

        // Logging-in the created user
        String jwt = auxUtility.authenticate(signUpRequest.getUsername(), signUpRequest.getPassword());

        AuthenticatedUserDTO authenticatedUserDTO = dtoService.create(jwt, user.getUsername());


        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(authenticatedUserDTO);

    }

}
