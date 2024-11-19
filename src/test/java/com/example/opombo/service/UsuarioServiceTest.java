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
    @DisplayName("Não deve ser permitido o cadastro de um e-mail que já esteja registrado.")
    public void testCreate$EmailJaExistente() throws PomboException {
        Usuario usuarioNoBanco = UsuarioFactory.createUsuario();
        when(usuarioRepository.findByEmail(usuarioNoBanco.getEmail())).thenReturn(Optional.of(usuarioNoBanco));

        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setEmail(usuarioNoBanco.getEmail());

        assertThatThrownBy(() -> usuarioService.criar(usuario)).isInstanceOf(PomboException.class)
                .hasMessageContaining("Email ja cadastrado.");
    }
    @Test
    @DisplayName("Não deve ser permitido o cadastro de um CPF que já esteja registrado.")
    public void testCreate$CpfJaExistente() throws PomboException {
        Usuario usuarioNoBanco = UsuarioFactory.createUsuario();
        when(usuarioRepository.findByCpf(usuarioNoBanco.getCpf())).thenReturn(Optional.of(usuarioNoBanco));

        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setCpf(usuarioNoBanco.getCpf());

        assertThatThrownBy(() -> usuarioService.criar(usuario)).isInstanceOf(PomboException.class)
                .hasMessageContaining("CPF ja cadastrado.");
    }
    @Test
    @DisplayName("Deve ser possível encontrar um usuario por id")
    public void testFindById$success() throws PomboException {
        Usuario novoUsuario = UsuarioFactory.createUsuario();
        novoUsuario.setId("1");

        when(usuarioRepository.findById(("1"))).thenReturn(Optional.of(novoUsuario));
        Usuario resultado = usuarioService.buscarPorId(novoUsuario.getId());

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(novoUsuario.getId());
    }
    @Test
    @DisplayName("Deve ser possível atualizar um usuário")
    public void testUpdate$success() throws PomboException {
        Usuario novoUsuario = UsuarioFactory.createUsuario();
        novoUsuario.setId("1");

        when(usuarioRepository.findById(("1"))).thenReturn(Optional.of(novoUsuario));
        when(usuarioRepository.save(novoUsuario)).thenReturn(novoUsuario);
        Usuario salvadoNovoUsuario = usuarioService.criar(novoUsuario);

        assertThat(salvadoNovoUsuario.getId()).isEqualTo(novoUsuario.getId());
        assertThat(salvadoNovoUsuario.getNome()).isEqualTo(novoUsuario.getNome());

        novoUsuario.setNome("nome atualizado");

        Usuario resultado = usuarioService.atualizar(novoUsuario);

        assertThat(resultado.getNome()).isEqualTo("nome atualizado");

    }
}
