package com.example.opombo.model.entity;

import com.example.opombo.model.enums.Motivo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @ManyToOne
    @JoinColumn(name = "publicacao_id")
    private Publicacao publicacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotNull
    private Motivo motivo;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}