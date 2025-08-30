package br.com.desafio.tecnico.desafio.domain.entity.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record SupplierRequestUpdateDto(
        @NotBlank
        Long supplierId,
         String document,
         String name,
         String email,
         String cep,
        Integer type,
         String rg,
         LocalDate birthDate,
         Set<Long> enterprises
) {
}
