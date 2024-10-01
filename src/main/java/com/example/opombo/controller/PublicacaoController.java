package com.example.opombo.controller;

import com.example.opombo.exception.PomboException;
import com.example.opombo.model.dto.PublicacaoDTO;
import com.example.opombo.model.entity.Publicacao;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.seletor.PublicacaoSeletor;
import com.example.opombo.service.PublicacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicacoes")
public class PublicacaoController {

    @Autowired
    private PublicacaoService publicacaoService;

    @PostMapping
    public ResponseEntity<Publicacao> criarPublicacao(@Valid @RequestBody Publicacao publicacao) throws PomboException {
        Publicacao criada = publicacaoService.criar(publicacao);
        return ResponseEntity.ok(criada);
    }

    @PostMapping("/filtro")
    public ResponseEntity<List<Publicacao>> buscarPublicacoesComFiltro(@RequestBody PublicacaoSeletor seletor) {
        List<Publicacao> publicacoes = publicacaoService.buscarComFiltro(seletor);
        return ResponseEntity.ok(publicacoes);
    }

    @PostMapping("/curtir")
    public ResponseEntity<Void> curtir(@RequestParam String usuarioId, @RequestParam String publicacaoId) throws PomboException {
        publicacaoService.curtir(usuarioId, publicacaoId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/bloquear")
    public ResponseEntity<Void> bloquear(@RequestParam String usuarioId, @RequestParam String publicacaoId) throws PomboException {
        publicacaoService.bloquear(usuarioId, publicacaoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Publicacao>> listarTodasPublicacoes() {
        List<Publicacao> publicacoes = publicacaoService.buscarTodas();
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
}