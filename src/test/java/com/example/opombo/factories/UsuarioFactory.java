package com.example.opombo.factories;

import com.example.opombo.model.entity.Usuario;
import com.example.opombo.utils.GeradorCPF;

import java.util.UUID;

public class UsuarioFactory {
    public static Usuario createUsuario() {
        Usuario user = new Usuario();
        user.setNome("Nome");
        user.setEmail(UUID.randomUUID() + "@teste.com");
        user.setCpf(GeradorCPF.generateValidCPF());
        
        return user;
    }
}
