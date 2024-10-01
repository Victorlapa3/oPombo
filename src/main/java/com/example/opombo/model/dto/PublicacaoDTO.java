package com.example.opombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicacaoDTO {
    private String id;
    private String conteudo;
    private String usuarioId;
    private String nomeUsuario;
    private Integer qtdCurtidas;
    private Integer qtdDenuncias;

}
