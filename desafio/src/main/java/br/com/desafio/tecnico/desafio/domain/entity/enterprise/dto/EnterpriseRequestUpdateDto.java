package br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record EnterpriseRequestUpdateDto(
        @NotNull
        Long enterpriseId,
        @NotBlank
        String cnpj,
        String tradeName,
        @NotBlank
        String cep,
        Set<Long> suppliers
) {


}
