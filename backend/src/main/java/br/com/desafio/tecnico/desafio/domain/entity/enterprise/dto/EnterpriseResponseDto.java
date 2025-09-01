package br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto;

import br.com.desafio.tecnico.desafio.domain.entity.supplier.dto.SupplierResponseDto;
import br.com.desafio.tecnico.desafio.presentation.serializer.view.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;


import java.util.Set;
public record EnterpriseResponseDto(
        @JsonView(JsonViews.Default.class)
        Long enterpriseId,
        @JsonView(JsonViews.Default.class)
        String cnpj,
        @JsonView(JsonViews.Default.class)
        String tradeName,
        @JsonView(JsonViews.Default.class)
        String state,
        @JsonView(JsonViews.Default.class)
        String cep,
        @JsonView(JsonViews.SupplierWithEnterprises.class)
        Set<SupplierResponseDto> suppliers
) {
}
