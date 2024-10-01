package com.example.opombo.service;

import com.example.opombo.exception.PomboException;
import com.example.opombo.model.dto.PublicacaoDTO;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.repository.DenunciaRepository;
import com.example.opombo.model.repository.PublicacaoRepository;
import com.example.opombo.model.repository.UsuarioRepository;
import com.example.opombo.model.seletor.PublicacaoSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    public List<Publicacao> buscarTodas() {
        return publicacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

    public Publicacao buscarPorId(String id) throws PomboException {
        return publicacaoRepository.findById(id)
                .orElseThrow(() -> new PomboException("Publicação não encontrada."));
    }

    public Publicacao criar(Publicacao publicacao) throws PomboException {
        this.usuarioRepository.findById(publicacao.getUsuario().getId())
                .orElseThrow(() -> new PomboException("Usuário inválido."));

        return publicacaoRepository.save(publicacao);
    }

    // Se a publicação já estiver curtida, o métodoo a descurtirá
    public void curtir(String usuarioId, String publicacaoId) throws PomboException {
        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
                .orElseThrow(() -> new PomboException("Publicação não encontrada."));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new PomboException("Usuário não encontrado."));

        List<Usuario> curtidas = publicacao.getCurtidas();

        if (curtidas.contains(usuario)) {
            curtidas.remove(usuario);
        } else {
            curtidas.add(usuario);
        }

        publicacao.setCurtidas(curtidas);
        publicacaoRepository.save(publicacao);
    }

    public List<Usuario> buscarCurtidasPublicacao(String publicacaoId) throws PomboException {
        Publicacao publicacao = publicacaoRepository.findById(publicacaoId).orElseThrow(() -> new PomboException("Publicação não encontrada."));

        return publicacao.getCurtidas();
    }

    public List<Denuncia> buscarDenunciasPublicacao(String publicacaoId) throws PomboException {
        Publicacao publicacao = publicacaoRepository.findById(publicacaoId).orElseThrow(() -> new PomboException("Publicação não encontrada."));

        return publicacao.getDenuncias();
    }

    public List<PublicacaoDTO> buscarDTO() throws PomboException {
        List<Publicacao> publicacoes = this.buscarTodas();
        List<PublicacaoDTO> dtos = new ArrayList<>();

        for(Publicacao p : publicacoes) {
            Integer qtdCurtidas = this.buscarCurtidasPublicacao(p.getId()).size();
            Integer qtdDenuncias = this.buscarDenunciasPublicacao(p.getId()).size();
            PublicacaoDTO dto = Publicacao.toDTO(p, qtdCurtidas, qtdDenuncias);
            dtos.add(dto);
        }

        return dtos;
    }

    // Se a publicação já estiver bloqueada, o métoodo a desbloqueará
    public void bloquear(String usuarioId, String publicacaoId) throws PomboException {
        verificarAdministrador(usuarioId);

        List<Denuncia> denuncias = this.denunciaRepository.findByPublicacaoId(publicacaoId);

        Publicacao publicacao = publicacaoRepository.findById(publicacaoId).orElseThrow(() -> new PomboException("A publicação não foi encontrada."));

        if(denuncias.isEmpty()) {
            throw new PomboException("A publicação não foi denunciada");
        }

        publicacao.setBloqueado(!publicacao.isBloqueado());

        publicacaoRepository.save(publicacao);
    }

    public List<Publicacao> buscarComFiltro(PublicacaoSeletor seletor) {
        if (seletor.temPaginacao()) {
            int numeroPagina = seletor.getPagina();
            int tamanhoPagina = seletor.getLimite();

            PageRequest pagina = PageRequest.of(numeroPagina - 1, tamanhoPagina, Sort.by(Sort.Direction.DESC, "criadoEm"));
            return publicacaoRepository.findAll(seletor, pagina).toList();
        }

        return publicacaoRepository.findAll(seletor, Sort.by(Sort.Direction.DESC, "criadoEm"));
    }

//    public List<Usuario> buscarCurtidasPublicacao(String publicacaoId) throws PomboException {
//        Publicacao publicacao = publicacaoRepository.findById(publicacaoId)
//                .orElseThrow(() -> new PomboException("Publicação não encontrada."));
//
//        return publicacao.getCurtidas();
//    }

    public boolean deletar(String id) {
        publicacaoRepository.deleteById(id);
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