package com.example.opombo.model.entity;

import com.example.opombo.model.entity.Usuario;
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
    @Size(max = 300, message = "O conteúdo do Pruu deve conter no máximo 300 caracteres.")
    private String conteudo;

    @ManyToMany
    @JoinTable(
            name = "publicacao_like",
            joinColumns = @JoinColumn(name = "publicacao_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> curtidas;

    private boolean bloqueado = false;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}
