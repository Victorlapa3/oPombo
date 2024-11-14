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
import com.example.opombo.utils.RSAEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacaoService {

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private RSAEncoder rsaEncoder;

    @Autowired
    private ImagemService imagemService;

    public void salvarFotoPublicacao(MultipartFile imagem, String publicacaoId, String usuarioId) throws PomboException {

        Publicacao publicacaoComNovaImagem = publicacaoRepository
                .findById(publicacaoId)
                .orElseThrow(() -> new PomboException("Publicação não encontrada"));

        if(!publicacaoComNovaImagem.getUsuario().getId().equals(usuarioId)){
            throw new PomboException("Você não pode alterar a imagem do Pruu de outro usuário.");
        }

        String imagemBase64 = imagemService.processarImagem(imagem);

        publicacaoComNovaImagem.setPublicacaoImagem(imagemBase64);

        publicacaoRepository.save(publicacaoComNovaImagem);
    }

    public Publicacao criar(Publicacao publicacao) throws PomboException {
        if(publicacao.getConteudo().length() > 300) {
            throw new PomboException("O conteúdo da Publicacao deve conter no máximo 300 caracteres.");
        }

        publicacao.setConteudo(rsaEncoder.encode(publicacao.getConteudo()));

        return publicacaoRepository.save(publicacao);
    }

    public Publicacao buscarPorId(String id) throws PomboException {
        Publicacao publicacao = publicacaoRepository.findById(id).orElseThrow(() -> new PomboException("Publicação não encontrada."));

        publicacao.setConteudo(rsaEncoder.decode(publicacao.getConteudo()));


        return publicacao;
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
        List<Publicacao> publicacoes = publicacaoRepository.findAll();
        List<PublicacaoDTO> dtos = new ArrayList<>();

        for(Publicacao p : publicacoes) {
            Integer qtdCurtidas = this.buscarCurtidasPublicacao(p.getId()).size();
            Integer qtdDenuncias = this.buscarDenunciasPublicacao(p.getId()).size();
            PublicacaoDTO dto = Publicacao.toDTO(p, qtdCurtidas, qtdDenuncias);
            dtos.add(dto);
        }

        return dtos;
    }

    public List<Publicacao> buscarComFiltro(PublicacaoSeletor seletor) {
        List<Publicacao> publicacoes = new ArrayList<>();

        if (seletor.temPaginacao()) {
            int numeroPagina = seletor.getPagina();
            int tamanhoPagina = seletor.getLimite();

            PageRequest pagina = PageRequest.of(numeroPagina - 1, tamanhoPagina, Sort.by(Sort.Direction.DESC, "criadoEm"));
            publicacoes = publicacaoRepository.findAll(seletor, pagina).toList();
        } else {
            publicacoes = publicacaoRepository.findAll(seletor, Sort.by(Sort.Direction.DESC, "criadoEm"));
        }

        publicacoes = removerPublicacoesBloqueadasEExcluidas(publicacoes);

        return publicacoes;
    }

    public void deletar(String publicacaoId, String usuarioId) throws PomboException {
        Publicacao publicacao = publicacaoRepository.findById(publicacaoId).orElseThrow(() -> new PomboException("A publicação não foi encontrada."));

        if(!publicacao.getUsuario().getId().equals(usuarioId)) {
            throw new PomboException("Você não pode excluir publicações de outros usuários.");
        }

        publicacao.setExcluido(true);
        publicacaoRepository.save(publicacao);
    }

    public void verificarAdministrador(String usuarioId) throws PomboException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new PomboException("Usuário não encontrado."));

        if (usuario.getPapel() == Papel.USUARIO) {
            throw new PomboException("Usuário não autorizado.");
        }
    }

    public List<Publicacao> removerPublicacoesBloqueadasEExcluidas(List<Publicacao> publicacoes) {
        return publicacoes.stream()
                .filter(pub -> !pub.isBloqueado() && !pub.isExcluido())
                .collect(Collectors.toList());
    }
}