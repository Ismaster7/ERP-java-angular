package br.com.desafio.tecnico.desafio.domain.entity.enterprise;

import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record EnterpriseRequestDto(

        Long enterpriseId,
        @NotBlank
        String cnpj,
        String tradeName,
        String cep,
        Set<Long> suppliers
) {
}
