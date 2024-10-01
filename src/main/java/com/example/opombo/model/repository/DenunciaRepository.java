package com.example.opombo.model.repository;

import com.example.opombo.model.entity.Denuncia;
import com.example.opombo.model.entity.Publicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String>, JpaSpecificationExecutor<Denuncia> {
    List<Denuncia> findByPublicacaoId(String publicacaoId);
}