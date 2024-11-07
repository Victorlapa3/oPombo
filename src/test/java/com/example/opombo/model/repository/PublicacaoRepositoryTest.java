package com.example.opombo.model.repository;

import com.example.opombo.factories.PublicacaoFactory;
import com.example.opombo.factories.UsuarioFactory;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles
public class PublicacaoRepositoryTest {

    @Autowired
    private PublicacaoRepository publicacaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Nao deve ser possivel criar uma publicacao com conteúdo com mais de 300 caracteres")
    public void testCreate$conteudoMaiorDoQue300Caracteres() {
        Usuario user = UsuarioFactory.createUsuario();
        usuarioRepository.save(user);

        Publicacao publicacao = PublicacaoFactory.createPublicacao(user);
        String conteudo = "a";
        publicacao.setConteudo(conteudo.repeat(301));

        assertThatThrownBy(() -> publicacaoRepository.saveAndFlush(publicacao)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O conteúdo da Publicacao deve conter no máximo 300 caracteres.");
    }
}
