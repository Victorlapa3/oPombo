package com.example.opombo.service;

import com.example.opombo.model.dto.DenunciaDTO;
import com.example.opombo.exception.PomboException;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.enums.Motivo;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.enums.Situacao;
import com.example.opombo.model.repository.DenunciaRepository;
import com.example.opombo.model.repository.PublicacaoRepository;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.model.seletor.DenunciaSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Denuncia criar(Denuncia denuncia) throws PomboException {
        if(denuncia.getMotivo() == null || !EnumSet.allOf(Motivo.class).contains(denuncia.getMotivo())){
            throw new PomboException("O motivo da denuncia deve ser valido");
        }

        return denunciaRepository.save(denuncia);
    }

    public void alterarStatus(String denunciaId, Situacao novaSituacao) throws PomboException {
        Denuncia denuncia = denunciaRepository.findById(denunciaId).orElseThrow(() -> new PomboException("Denuncia nao encontrada"));
        Publicacao publicacao = denuncia.getPublicacao();

        if(novaSituacao == Situacao.ACEITADA) {
            publicacao.setBloqueado(true);

            publicacaoRepository.save(publicacao);
        }

        if(denuncia.getSituacao() == Situacao.ACEITADA && (novaSituacao == Situacao.RECUSADA || novaSituacao == Situacao.PENDENTE)) {
            publicacao.setBloqueado(false);

            publicacaoRepository.save(publicacao);
        }

        denuncia.setSituacao(novaSituacao);

        denunciaRepository.save(denuncia);
    }

    public List<Denuncia> buscarTodas() throws PomboException {
        return denunciaRepository.findAll(Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

    public Denuncia buscarPorId(String denunciaId) throws PomboException {
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

    public DenunciaDTO gerarRelatorio(String usuarioId, String publicacaoId) throws PomboException {
        // verificarAdministrador(usuarioId);
        List<Denuncia> denuncias = this.denunciaRepository.findByPublicacaoId(publicacaoId);
        List<Denuncia> denunciasPendentes = new ArrayList<>();
        List<Denuncia> denunciasRecusadas = new ArrayList<>();
        List<Denuncia> denunciasAceitadas = new ArrayList<>();

        for (Denuncia d : denuncias) {
            if (d.getSituacao() == Situacao.PENDENTE) {
                denunciasPendentes.add(d);
            }
            if (d.getSituacao() == Situacao.RECUSADA) {
                denunciasRecusadas.add(d);
            }
            if (d.getSituacao() == Situacao.ACEITADA) {
                denunciasAceitadas.add(d);
            }
        }

        return Denuncia.toDTO(publicacaoId, denuncias.size(), denunciasPendentes.size(), denunciasRecusadas.size(), denunciasAceitadas.size());
    }

}