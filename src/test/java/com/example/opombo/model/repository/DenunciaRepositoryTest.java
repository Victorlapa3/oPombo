package com.example.opombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.opombo.factories.PublicacaoFactory;
import com.example.opombo.factories.UsuarioFactory;
import jakarta.validation.ConstraintViolationException;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles
public class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Test
    @DisplayName("Não deve ser possível criar uma denúncia sem nenhum motivo")
    public void testCreate$denunciaSemMotivo() {
        Usuario user = UsuarioFactory.createUsuario();
        Publicacao publicacao = PublicacaoFactory.createPublicacao(user);

        usuarioRepository.saveAndFlush(user);
        publicacaoRepository.saveAndFlush(publicacao);

        Denuncia denuncia = new Denuncia();
        denuncia.setUsuario(user);
        denuncia.setPublicacao(publicacao);

        assertThatThrownBy(() -> denunciaRepository.saveAndFlush(denuncia)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("A denuncia deve possuir um motivo!");


    }
}
