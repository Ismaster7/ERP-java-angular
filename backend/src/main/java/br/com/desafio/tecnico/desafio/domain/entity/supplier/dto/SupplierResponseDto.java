package br.com.desafio.tecnico.desafio.domain.entity.supplier.dto;

import br.com.desafio.tecnico.desafio.domain.entity.enterprise.Enterprise;
import br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto.EnterpriseResponseDto;
import br.com.desafio.tecnico.desafio.presentation.serializer.view.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDate;
import java.util.Set;

public record SupplierResponseDto(
        @JsonView(JsonViews.Default.class)
        Long supplierId,

        @JsonView(JsonViews.Default.class)
        String document,

        @JsonView(JsonViews.Default.class)
        String name,

        @JsonView(JsonViews.Default.class)
        String email,

        @JsonView(JsonViews.Default.class)
        String cep,

        @JsonView(JsonViews.Default.class)
        Integer type,

        @JsonView(JsonViews.Default.class)
        String rg,

        @JsonView(JsonViews.Default.class)
        LocalDate birthDate,

        @JsonView(JsonViews.SupplierWithEnterprises.class)
        Set<EnterpriseResponseDto> enterprises
) {}
