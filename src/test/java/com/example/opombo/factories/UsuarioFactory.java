package com.example.opombo.factories;

import com.example.opombo.model.entity.Usuario;

import java.util.UUID;

public class UsuarioFactory {
    public static Usuario createUsuario() {
        Usuario user = new Usuario();
        user.setNome("Nome");
        user.setEmail(UUID.randomUUID() + "@teste.com");
        user.setCpf("12833057989");
        
        return user;
    }
}
