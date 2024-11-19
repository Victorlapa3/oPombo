package com.example.opombo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.opombo.exception.PomboException;
import com.example.opombo.factories.DenunciaFactory;
import com.example.opombo.factories.PublicacaoFactory;
import com.example.opombo.factories.UsuarioFactory;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.DenunciaRepository;
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
@ActiveProfiles
public class DenunciaServiceTest {
    @Mock private DenunciaRepository denunciaRepository;

    @Mock private PublicacaoRepository publicacaoRepository;

    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private DenunciaService denunciaService;

    Usuario usuario;
    Publicacao publicacao;

    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.createUsuario();
        usuario.setId("1");
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));

        Publicacao publicacao = PublicacaoFactory.createPublicacao(usuario);
        publicacao.setId("2");
        when(publicacaoRepository.findById("2")).thenReturn(Optional.of(publicacao));
    }

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
        publicacaoRepository.deleteAll();
        denunciaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possível criar uma nova denúncia")
    public void testCriarDenuncia$sucess() throws PomboException {
        Denuncia denuncia = DenunciaFactory.createDenuncia(usuario, publicacao);
        when(denunciaRepository.save(denuncia)).thenReturn(denuncia);
        Denuncia resultado = denunciaService.criar(denuncia);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getUsuario().getId()).isEqualTo("1");
    }
    @Test
    @DisplayName("O motivo da denúncia deve ser válido")
    public void testCriarDenuncia$fail() throws PomboException {
        Denuncia denuncia = DenunciaFactory.createDenuncia(usuario, publicacao);
        denuncia.setMotivo(null);

        assertThatThrownBy(() -> denunciaService.criar(denuncia)).isInstanceOf(PomboException.class)
                .hasMessageContaining("O motivo da denuncia deve ser valido");
    }

}
