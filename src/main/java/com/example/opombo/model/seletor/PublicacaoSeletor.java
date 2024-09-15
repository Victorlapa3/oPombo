package com.example.opombo.model.seletor;

import com.example.opombo.model.entity.Publicacao;
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
public class PublicacaoSeletor extends BaseSeletor implements Specification<Publicacao> {

    private String usuarioId;
    private String conteudo;
    private LocalDateTime criadoEmInicio;
    private LocalDateTime criadoEmFim;

    @Override
    public Predicate toPredicate(Root<Publicacao> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicados = new ArrayList<>();

        if(this.getConteudo() != null && !this.getConteudo().trim().isEmpty()) {
            // WHERE/AND  COLUNA   OPERADOR         VALOR
            //   where    conteudo   like  '%substring do conteudo%'
            predicados.add(cb.like(root.get("conteudo"), "%" + this.getConteudo() + "%"));
        }

        if(this.getUsuarioId() != null) {
            predicados.add(cb.equal(root.get("usuario").get("id"), this.getUsuarioId()));
        }

        aplicarFiltroPorData(root, cb, predicados, this.getCriadoEmInicio(), this.getCriadoEmFim(), "criadoEm");

        return cb.and(predicados.toArray(new Predicate[0]));
    }
}