package com.example.opombo.factories;

import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;

import java.util.ArrayList;

public class PublicacaoFactory {
    public static Publicacao createPublicacao(Usuario usuario) {
        Publicacao p = new Publicacao();

        p.setUsuario(usuario);
        p.setConteudo("Exemplo exemplo exemplo");
        p.setCurtidas(new ArrayList<>());
        p.setDenuncias(new ArrayList<>());

        return p;
    }
}
