package br.com.desafio.tecnico.desafio.infraestructure.repository.specifications;


import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecifications {
    public static Specification<Supplier> nameLike(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) return builder.conjunction();
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Supplier> documentLike(String document) {
        return (root, query, builder) -> {
            if (document == null || document.isEmpty()) return builder.conjunction();
            String cleaned = document.replaceAll("\\D", "");
            return builder.like(root.get("document").get("document"), "%" + cleaned + "%");
        };
    }


}