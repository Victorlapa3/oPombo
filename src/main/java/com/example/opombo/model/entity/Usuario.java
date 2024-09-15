package com.example.opombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.example.opombo.model.enums.Papel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String id;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference
    private List<Publicacao> publicacoes;

    @NotBlank
    private String nome;

    @NotBlank
    @Email(message = "O email deve ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank
    @CPF
    @Column(unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Papel papel = Papel.USUARIO; // Se o valor não for informado, o padrão é USUARIO

    @CreationTimestamp
    private LocalDateTime criadoEm;
}