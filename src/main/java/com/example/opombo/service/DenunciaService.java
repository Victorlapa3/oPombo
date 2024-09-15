package com.example.opombo.service;

import com.example.opombo.exception.PomboException;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.DenunciaRepository;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.model.seletor.DenunciaSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia criar(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> buscarTodas(String usuarioId) throws PomboException {
        verificarAdministrador(usuarioId);
        return denunciaRepository.findAll(Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

    public Denuncia buscarPorId(String denunciaId, String usuarioId) throws PomboException {
        verificarAdministrador(usuarioId);
        return denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new PomboException("Denúncia não encontrada."));
    }

    public List<Denuncia> buscarComFiltro(DenunciaSeletor seletor) {
        if (seletor.temPaginacao()) {
            int numeroPagina = seletor.getPagina();
            int tamanhoPagina = seletor.getLimite();

            PageRequest pagina = PageRequest.of(numeroPagina - 1, tamanhoPagina, Sort.by(Sort.Direction.DESC, "criadoEm"));
            return denunciaRepository.findAll(seletor, pagina).toList();
        }

        return denunciaRepository.findAll(seletor, Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

    public boolean deletar(String id) {
        denunciaRepository.deleteById(id);
        return true;
    }

    public void verificarAdministrador(String usuarioId) throws PomboException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new PomboException("Usuário não encontrado."));

        if (usuario.getPapel() == Papel.USUARIO) {
            throw new PomboException("Usuário não autorizado.");
        }
    }
}