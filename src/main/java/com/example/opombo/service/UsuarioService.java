package com.example.opombo.service;

import com.example.opombo.exception.PomboException;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.model.seletor.UsuarioSeletor;
import org.hibernate.validator.internal.constraintvalidators.bv.NotNullValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criar(Usuario usuario) throws PomboException {
        verificarSeUsuarioExiste(usuario);
        padronizarCpf(usuario);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) throws PomboException {
        verificarSeUsuarioExiste(usuario);
        padronizarCpf(usuario);

        Usuario usuarioNoBanco = usuarioRepository.findById(usuario.getId()).orElseThrow(() -> new PomboException("Usuario nao encontrado"));

        usuario.setPapel(usuarioNoBanco.getPapel());
        usuario.setCriadoEm(usuarioNoBanco.getCriadoEm());

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
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

    private void verificarSeUsuarioExiste(Usuario usuario) throws PomboException {
        Optional<Usuario> usuarioComMesmoEmail = usuarioRepository.findByEmail(usuario.getEmail());
        Optional<Usuario> usuarioComMesmoCpf = usuarioRepository.findByCpf(usuario.getCpf());

        if(usuario.getId() == null) {
            usuario.setId("");
        }

        if(usuarioComMesmoEmail.isPresent()) {
            if(!usuario.getId().equals(usuarioComMesmoEmail.get().getId())) {
                throw new PomboException("Email ja cadastrado.");
            }
        }

        if(usuarioComMesmoCpf.isPresent()) {
            if(!usuario.getId().equals(usuarioComMesmoCpf.get().getId())) {
                throw new PomboException("CPF ja cadastrado.");
            }
        }
    }
}