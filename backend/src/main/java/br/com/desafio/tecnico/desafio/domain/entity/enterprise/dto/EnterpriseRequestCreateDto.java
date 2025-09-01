package br.com.desafio.tecnico.desafio.domain.entity.enterprise.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record EnterpriseRequestCreateDto(

        Long enterpriseId,
        @NotBlank
        String cnpj,
        @NotBlank
        String tradeName,
        @NotBlank
        String cep,
        Set<Long> suppliers
) {
}
