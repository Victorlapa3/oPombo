package com.example.opombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.example.opombo.model.enums.Papel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "O nome nao pode ficar em branco")
    @Size(max = 200, message = "O nome deve ter no maximo 200 caracteres.")
    private String nome;

    @NotBlank(message = "O email nao pode ficar em branco")
    @Email(message = "O email deve ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "O cpf nao pode ficar em branco")
    @CPF(message = "O cpf deve ser válido.")
    @Column(unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Papel papel = Papel.USUARIO; // Se o valor não for informado, o padrão é USUARIO

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference(value = "usuario_publicacao")
    private List<Publicacao> publicacoes;

    @OneToMany(mappedBy = "usuario")
    @JsonBackReference(value = "usuario_denuncia")
    private List<Denuncia> denuncias;

    @CreationTimestamp
    private LocalDateTime criadoEm;
}