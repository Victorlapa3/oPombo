package com.example.opombo.model.seletor;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public abstract class BaseSeletor {

    private int pagina;
    private int limite;

    public BaseSeletor() {
        this.limite = 0;
        this.pagina = 0;
    }

    public boolean temPaginacao() {
        return this.limite > 0 && this.pagina > 0;
    }

    public boolean isStringValida(String texto) {
        return texto != null && !texto.isBlank();
    }

    public static void aplicarFiltroPorData(Root<?> root,
                                            CriteriaBuilder cb, List<Predicate> predicados,
                                            LocalDateTime dataInicio, LocalDateTime dataFim, String nomeAtributo) {
        if (dataInicio != null && dataFim != null) {
            predicados.add(cb.between(root.get(nomeAtributo), dataInicio, dataFim));
        } else if (dataInicio != null) {
            predicados.add(cb.greaterThanOrEqualTo(root.get(nomeAtributo), dataInicio));
        } else if (dataFim != null) {
            predicados.add(cb.lessThanOrEqualTo(root.get(nomeAtributo), dataFim));
        }
    }
}