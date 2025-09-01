package br.com.desafio.tecnico.desafio.infraestructure.repository;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cnpj;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long>, JpaSpecificationExecutor {
    Optional<Enterprise> findByCnpj(Cnpj cnpj);
    boolean existsByCnpj(Cnpj cnpj);
    boolean existsByTradeName(String tradeName);

//    @Query("""
//       SELECT e\s
//       FROM Enterprise e
//       WHERE (:tradeName IS NULL OR LOWER(e.tradeName) LIKE LOWER(CONCAT('%', :tradeName, '%')))
//         AND (:cnpj IS NULL OR e.cnpj.value LIKE CONCAT('%', :cnpj, '%'))
//      \s""")
//    Set<Enterprise> search(@Param("tradeName") String tradeName,
//                           @Param("cnpj") String cnpj);
}
