package com.example.opombo.model.repository;

import com.example.opombo.model.entity.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacaoRepository extends JpaRepository<Publicacao, String>, JpaSpecificationExecutor<Publicacao> {
}