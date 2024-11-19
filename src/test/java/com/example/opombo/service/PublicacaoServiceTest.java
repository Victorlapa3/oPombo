package com.example.opombo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.opombo.exception.PomboException;
import com.example.opombo.factories.PublicacaoFactory;
import com.example.opombo.factories.UsuarioFactory;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.PublicacaoRepository;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.utils.RSAEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PublicacaoServiceTest {
    @Mock
    private PublicacaoRepository publicacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RSAEncoder rsaEncoder;

    @InjectMocks
    private PublicacaoService publicacaoService;

    Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.createUsuario();
    }

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
        publicacaoRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar uma nova publicação")
    public void testCriarPublicacao$success() throws PomboException {
        Publicacao publicacao = PublicacaoFactory.createPublicacao(usuario);

        String conteudoOriginal = "Exemplo exemplo exemplo";
        String conteudoCriptografado = "conteudoCriptografado";

        publicacao.setConteudo(conteudoOriginal);

        when(rsaEncoder.encode(conteudoOriginal)).thenReturn(conteudoCriptografado);
        when(publicacaoRepository.save(publicacao)).thenReturn(publicacao);

        Publicacao resultado = publicacaoService.criar(publicacao);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getConteudo()).isEqualTo(conteudoCriptografado);
    }
    @Test
    @DisplayName("Não deve ser possível deletar publicação de outro usuário")
    public void testDeletarPublicacao$failure() throws PomboException {
        Usuario usuarioOriginal = UsuarioFactory.createUsuario();
        usuarioOriginal.setId("1");

        Publicacao publicacao = PublicacaoFactory.createPublicacao(usuarioOriginal);
        publicacao.setId("1");

        Usuario outroUsuario = UsuarioFactory.createUsuario();
        outroUsuario.setId("2");

        when(publicacaoRepository.findById("1")).thenReturn(Optional.of(publicacao));

        assertThatThrownBy(() -> publicacaoService.deletar("1", "2")).isInstanceOf(PomboException.class)
                .hasMessageContaining("Você não pode excluir publicações de outros usuários.");

    }
}
