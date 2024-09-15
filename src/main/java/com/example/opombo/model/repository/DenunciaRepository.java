package com.example.opombo.model.repository;

import com.example.opombo.model.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String>, JpaSpecificationExecutor<Denuncia> {

    Optional<Denuncia> findByPublicacaoId(String publicacaoId);
}