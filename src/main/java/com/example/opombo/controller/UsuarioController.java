package com.example.opombo.controller;

import com.example.opombo.exception.PomboException;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.seletor.UsuarioSeletor;
import com.example.opombo.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@Valid @RequestBody Usuario usuario) throws PomboException {
        Usuario criado = usuarioService.criar(usuario);
        return ResponseEntity.ok(criado);
    }

    @PostMapping("/filtro")
    public ResponseEntity<List<Usuario>> buscarUsuariosComFiltro(@RequestBody UsuarioSeletor seletor) {
        List<Usuario> usuarios = usuarioService.buscarComFiltro(seletor);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping
    public ResponseEntity<Usuario> atualizarUsuario(@Valid @RequestBody Usuario usuario) throws PomboException {
        Usuario atualizado = usuarioService.atualizar(usuario);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable String id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }
}