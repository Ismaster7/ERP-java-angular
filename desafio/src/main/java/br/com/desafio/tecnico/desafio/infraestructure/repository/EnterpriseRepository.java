package br.com.desafio.tecnico.desafio.infraestructure.repository;

import br.com.desafio.tecnico.desafio.domain.entity.Enterprise;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    Optional<Enterprise> findByCnpj(Cnpj cnpj);
    boolean existsByCnpj(Cnpj cnpj);
}
