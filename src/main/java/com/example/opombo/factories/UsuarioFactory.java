package com.example.opombo.factories;

import com.example.opombo.model.entity.Usuario;

public class UsuarioFactory {
    public static Usuario createUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setCpf("804.257.930-60");

        return usuario;
    }
}
