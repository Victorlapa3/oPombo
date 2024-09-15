package com.example.opombo.model.seletor;

import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.enums.Motivo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DenunciaSeletor extends BaseSeletor implements Specification<Denuncia> {

    private String usuarioId;
    private String publicacaoId;
    private Motivo motivo;
    private LocalDateTime criadoEmInicio;
    private LocalDateTime criadoEmFim;

    @Override
    public Predicate toPredicate(Root<Denuncia> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicados = new ArrayList<>();

        if(this.getUsuarioId() != null) {
            predicados.add(cb.equal(root.get("usuario").get("id"), this.getUsuarioId()));
        }

        if(this.getPublicacaoId() != null) {
            predicados.add(cb.equal(root.get("publicacao").get("id"), this.getPublicacaoId()));
        }

        if(this.getMotivo() != null) {
            predicados.add(cb.equal(root.get("motivo"), this.getMotivo()));
        }

        aplicarFiltroPorData(root, cb, predicados, this.getCriadoEmInicio(), this.getCriadoEmFim(), "criadoEm");

        return cb.and(predicados.toArray(new Predicate[0]));
    }
}