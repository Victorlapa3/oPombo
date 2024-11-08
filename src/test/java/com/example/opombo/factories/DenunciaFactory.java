package com.example.opombo.factories;

import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.enums.Motivo;

public class DenunciaFactory {
    public static Denuncia createDenuncia(Usuario usuario, Publicacao publicacao) {
        Denuncia d = new Denuncia();
        d.setUsuario(usuario);
        d.setPublicacao(publicacao);
        d.setMotivo(Motivo.GOLPE);

        return d;
    }
}
