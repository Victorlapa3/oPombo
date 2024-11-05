package com.example.opombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

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
        String name = "a";
        Usuario user = new Usuario();
        user.setNome(name.repeat(201));
        user.setEmail("a@gmail.com");
        user.setCpf("12833057989");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O nome deve ter no maximo 200 caracteres.");
    }

    @Test
    @DisplayName("Não deve ser possível criar um usuario com email inválido.")
    public void testCreate$emailInvalido() {
        Usuario user = new Usuario();
        user.setNome("lapada");
        user.setEmail("teste");
        user.setCpf("12833057989");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O email deve ser válido.");
    }

    @Test
    @DisplayName("Não deve ser possível criar um usuario com cpf inválido.")
    public void testCreate$cpfInvalido() {
        Usuario user = new Usuario();
        user.setNome("teste");
        user.setEmail("a@gmail.com");
        user.setCpf("0123456");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(user)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("O cpf deve ser válido.");
    }

}