package com.example.opombo.model.entity;

import com.example.opombo.model.dto.PublicacaoDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank
    @Size(max = 300, message = "O conteúdo da Publicacao deve conter no máximo 300 caracteres.")
    private String conteudo;

    @ManyToMany
    @JoinTable(
            name = "publicacao_like",
            joinColumns = @JoinColumn(name = "publicacao_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> curtidas;

    @OneToMany(mappedBy = "publicacao")
    @JsonBackReference
    private List<Denuncia> denuncias;

    private boolean bloqueado = false;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    public static PublicacaoDTO toDTO(Publicacao publicacao, Integer qtdCurtidas, Integer qtdDenuncias) {
        if(publicacao.isBloqueado()) {
        publicacao.setConteudo("Bloqueado pelo Pombo de Moraes(Administrador)");
        }

        return new PublicacaoDTO(
                publicacao.getId(),
                publicacao.getConteudo(),
                publicacao.getUsuario().getId(),
                publicacao.getUsuario().getNome(),
                qtdCurtidas,
                qtdDenuncias
                );
    }
}
