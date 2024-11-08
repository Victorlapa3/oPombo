package com.example.opombo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.opombo.exception.PomboException;
import com.example.opombo.factories.UsuarioFactory;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
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
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve ser possivel criar um usuário")
    public void testCreate$success() throws PomboException {
        Usuario usuario = UsuarioFactory.createUsuario();

        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario result = usuarioService.criar(usuario);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(usuario.getNome());
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar com um email já cadastrado")
    public void testCreate$EmailJaExistente() throws PomboException {
        Usuario usuarioNoBanco = UsuarioFactory.createUsuario();
        when(usuarioRepository.findByEmail(usuarioNoBanco.getEmail())).thenReturn(Optional.of(usuarioNoBanco));

        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setEmail(usuarioNoBanco.getEmail());

        assertThatThrownBy(() -> usuarioService.criar(usuario)).isInstanceOf(PomboException.class)
                .hasMessageContaining("Email ja cadastrado.");
    }
}
