package com.example.opombo.controller;

import com.example.opombo.auth.AuthService;
import com.example.opombo.exception.PomboException;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("autenticar")
    public String authenticate(Authentication authentication) {
        return authService.authenticate(authentication);
    }

    @Operation(summary = "Creates a new user",
            description = "Creates a new user, the request must receive at least name, email and CPF in the body",
            responses = {
                    @ApiResponse(responseCode = "200", description = "A new user has been successfully created"),
                    @ApiResponse(responseCode = "400", description = "Wrong or forgotten information on the body"),
                    @ApiResponse(responseCode = "409", description = "Email or CPF already registered")
            })
    @PostMapping("/registrar")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Usuario create(@Valid @RequestBody Usuario usuario) throws PomboException {

        String encryptedPassword = passwordEncoder.encode(usuario.getPassword());

        usuario.setSenha(encryptedPassword);

        if(usuario.getPapel().equals(Papel.ADMIN)) {
            usuario.setPapel(Papel.ADMIN);
        }

        return usuarioService.criar(usuario);
    }
}
