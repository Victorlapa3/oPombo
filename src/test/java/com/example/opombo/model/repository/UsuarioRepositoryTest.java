package com.example.opombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.opombo.factories.UsuarioFactory;
import com.example.opombo.model.entity.Usuario;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Nao deve ser possivel criar um usuario com nome maior que 200 caracteres.")
    public void testCreate$nomeMaiorDoQue200Caracteres() {
        Usuario user = UsuarioFactory.createUsuario();
        String name = "a";
        user.setNome(name.repeat(201));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O nome deve ter no maximo 200 caracteres.");
    }

    @Test
    @DisplayName("Não deve ser possível criar um usuario com email inválido.")
    public void testCreate$emailInvalido() {
        Usuario user = UsuarioFactory.createUsuario();
        user.setEmail("teste");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O email deve ser válido.");
    }

    @Test
    @DisplayName("Não deve ser possível criar um usuario com cpf inválido.")
    public void testCreate$cpfInvalido() {
        Usuario user = UsuarioFactory.createUsuario();
        user.setCpf("0123456");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O cpf deve ser válido.");
    }

}