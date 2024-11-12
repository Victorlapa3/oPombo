package com.example.opombo.controller;

import com.example.opombo.auth.AuthService;
import com.example.opombo.exception.PomboException;
import com.example.opombo.model.dto.DenunciaDTO;
import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Usuario;
import com.example.opombo.model.enums.Papel;
import com.example.opombo.model.enums.Situacao;
import com.example.opombo.model.seletor.DenunciaSeletor;
import com.example.opombo.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public Denuncia criarDenuncia(@Valid @RequestBody Denuncia denuncia) throws PomboException {
        return denunciaService.criar(denuncia);
    }

    @PatchMapping("/admin/atualizar-status/{denunciaId}/{novaSituacaoString}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable String denunciaId, @PathVariable String novaSituacaoString) throws PomboException {
        Usuario subject = authService.getAuthenticatedUser();
        Situacao novaSituacao = null;

        if(novaSituacaoString.equalsIgnoreCase("aceitada")){
            novaSituacao = Situacao.ACEITADA;
        } else if (novaSituacaoString.equalsIgnoreCase("recusada")){
            novaSituacao = Situacao.RECUSADA;
        } else if (novaSituacaoString.equalsIgnoreCase("pendente")){
            novaSituacao = Situacao.PENDENTE;
        } else {
            throw new PomboException("Nova situacao invalida");
        }

        if (subject.getPapel() == Papel.ADMIN) {
            denunciaService.alterarStatus(denunciaId, novaSituacao);
            return ResponseEntity.ok().build();
        } else {
            throw new PomboException("Usuário não autorizado.");
        }
    }


    @PostMapping("/filtro")
    public List<Denuncia> buscarDenunciasComFiltro(@RequestBody DenunciaSeletor seletor) {
        return denunciaService.buscarComFiltro(seletor);
    }

    @GetMapping("/dto")
    public DenunciaDTO gerarRelatorio(@RequestParam String adminId, @RequestParam String publicationId) throws PomboException {
        return denunciaService.gerarRelatorio(adminId, publicationId);
    }

    @GetMapping
    public List<Denuncia> listarTodasDenuncias(@RequestParam String userId) throws PomboException {
        return denunciaService.buscarTodas(userId);
    }

    @GetMapping("/{id}")
    public Denuncia buscarDenunciaPorId(@PathVariable String id, @RequestParam String userId) throws PomboException {
        return denunciaService.buscarPorId(id, userId);
    }

    @DeleteMapping("/{id}")
    public boolean deletarDenunciaPorId(@PathVariable String id) {
        // TODO: Adicionar validação de permissão
        return denunciaService.deletar(id);
    }
}