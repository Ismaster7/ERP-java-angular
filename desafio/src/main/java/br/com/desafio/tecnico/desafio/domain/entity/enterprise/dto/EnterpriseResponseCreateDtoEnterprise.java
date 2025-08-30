package br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto;

import br.com.desafio.tecnico.desafio.domain.entity.supplier.Supplier;

import java.util.Set;

public record EnterpriseResponseCreateDtoEnterprise(

        Long enterpriseId,
        String cnpj,
        String tradeName,
        String cep,
        Set<Supplier> suppliers
) {
}
