package br.com.desafio.tecnico.desafio.infraestructure.repository;

import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByDocument(String document);

    @Query(nativeQuery = true, value = "SELECT * FROM supplier f WHERE (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:document IS NULL OR f.document = :document) ")
    List<Supplier> findByNameAndDocument(@Param("name") String name);
}
