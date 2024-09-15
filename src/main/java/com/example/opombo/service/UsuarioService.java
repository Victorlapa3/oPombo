package com.example.opombo.service;

import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.model.seletor.UsuarioSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario criar(Usuario usuario) {
        padronizarCpf(usuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        padronizarCpf(usuario);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarComFiltro(UsuarioSeletor seletor) {
        if (seletor.temPaginacao()) {
            int numeroPagina = seletor.getPagina();
            int tamanhoPagina = seletor.getLimite();

            PageRequest pagina = PageRequest.of(numeroPagina - 1, tamanhoPagina);
            return usuarioRepository.findAll(seletor, pagina).toList();
        }

        return usuarioRepository.findAll(seletor);
    }

    public boolean deletar(String id) {
        usuarioRepository.deleteById(id);
        return true;
    }

    private void padronizarCpf(Usuario usuario) {
        String cpf = usuario.getCpf().replaceAll("[.\\-]", "");
        usuario.setCpf(cpf);
    }
}