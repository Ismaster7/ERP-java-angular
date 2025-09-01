package br.com.desafio.tecnico.desafio.infraestructure.repository.specifications;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import org.springframework.data.jpa.domain.Specification;

public class EnterpriseSpecifications {

    public static Specification<Enterprise> tradeNameLike(String tradeName) {
        return (root, query, cb) -> {
            if (tradeName == null || tradeName.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get("tradeName")), "%" + tradeName.toLowerCase() + "%");
        };
    }

    public static Specification<Enterprise> cnpjLike(String cnpj) {
        return (root, query, builder) -> {
            if (cnpj == null || cnpj.isEmpty()) return builder.conjunction();
            String cleaned = cnpj.replaceAll("\\D", ""); // remove pontuação, se necessário
            return builder.like(root.get("cnpj").get("document"), "%" + cleaned + "%");
        };
    }

    public static Specification<Enterprise> cnpjEqual(String cnpj) {
        return (root, query, builder) -> {
            if (cnpj == null || cnpj.isEmpty()) return builder.conjunction();
            String cleaned = cnpj.replaceAll("\\D", "");
            return builder.equal(root.get("cnpj").get("document"), cleaned);
        };
    }
}
