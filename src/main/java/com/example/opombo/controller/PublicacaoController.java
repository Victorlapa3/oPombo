package com.example.opombo.controller;

import com.example.opombo.auth.AuthService;
import com.example.opombo.exception.PomboException;
import com.example.opombo.model.dto.PublicacaoDTO;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.model.repository.PublicacaoRepository;
import com.example.opombo.model.seletor.PublicacaoSeletor;
import com.example.opombo.service.PublicacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/publicacoes")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;

    @Autowired
    private PublicacaoRepository publicacaoRepository;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<Publicacao> criarPublicacao(@Valid @RequestBody Publicacao publicacao) throws PomboException {
        Usuario subject = authService.getAuthenticatedUser();

        publicacao.getUsuario().setId(subject.getId());

        Publicacao criada = publicacaoService.criar(publicacao);
        return ResponseEntity.ok(criada);
    }

    @PostMapping("/upload-foto-publicacao")
    public void fazerUploadFotoPublicacao(@RequestParam("imagem") MultipartFile imagem, @RequestParam("publicacaoId") String publicacaoId) throws PomboException, IOException {
        Usuario usuario = authService.getAuthenticatedUser();

        if(imagem == null) {
            throw new PomboException("Arquivo inválido");
        }

        publicacaoService.salvarFotoPublicacao(imagem, publicacaoId, usuario.getId());
    }

    @PostMapping("/filtro")
    public ResponseEntity<List<Publicacao>> buscarPublicacoesComFiltro(@RequestBody PublicacaoSeletor seletor) {
        List<Publicacao> publicacoes = publicacaoService.buscarComFiltro(seletor);
        return ResponseEntity.ok(publicacoes);
    }

    @PostMapping("/curtir/{publicacaoId}")
    public boolean curtir(@PathVariable String publicacaoId) throws PomboException {
        Usuario subject = authService.getAuthenticatedUser();

        if(subject.getPapel() == Papel.USUARIO) {
            return publicacaoService.curtir(subject.getId(), publicacaoId);
        } else {
            throw new PomboException("Administradores não podem curtir publicações.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Publicacao>> listarTodasPublicacoes() {
        List<Publicacao> publicacoes = publicacaoRepository.findAll();
        return ResponseEntity.ok(publicacoes);
    }

    @GetMapping("/curtidas")
    public ResponseEntity<List<Usuario>> buscarCurtidasPorPublicacao(@RequestParam String publicacaoId) throws PomboException {
        List<Usuario> curtidas = publicacaoService.buscarCurtidasPublicacao(publicacaoId);
        return ResponseEntity.ok(curtidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publicacao> buscarPublicacaoPorId(@PathVariable String id) throws PomboException {
        Publicacao publicacao = publicacaoService.buscarPorId(id);
        return ResponseEntity.ok(publicacao);
    }
    @GetMapping("/relatorio")
    public List<PublicacaoDTO> gerarRelatorio() throws PomboException {
        List <PublicacaoDTO> publicacoesDTOS = publicacaoService.buscarDTO();
        return publicacoesDTOS;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws PomboException {
        Usuario subject = authService.getAuthenticatedUser();

        publicacaoService.deletar(id, subject.getId());

        return ResponseEntity.ok().build();
    }
}