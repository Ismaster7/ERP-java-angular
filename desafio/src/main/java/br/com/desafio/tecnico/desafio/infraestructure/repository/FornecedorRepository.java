package br.com.desafio.tecnico.desafio.infraestructure.repository;

import br.com.desafio.tecnico.desafio.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByDocumento(String documento);

    @Query(nativeQuery = true, value = "SELECT * FROM fornecedor f WHERE (:nome IS NULL OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:documento IS NULL OR f.documento = :documento) ")
    List<Fornecedor> findByNomeAndDocumento(@Param("nome") String nome);
}
