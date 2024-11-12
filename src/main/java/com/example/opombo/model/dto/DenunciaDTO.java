package com.example.opombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DenunciaDTO {
    private String idPublicacao;
    private Integer qtdDenuncias;
    private Integer qtdDenunciasPendentes;
    private Integer qtdDenunciasRecusadas;
    private Integer qtdDenunciasAceitadas;
}
