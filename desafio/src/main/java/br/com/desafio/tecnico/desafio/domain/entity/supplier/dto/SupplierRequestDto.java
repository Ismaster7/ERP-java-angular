package br.com.desafio.tecnico.desafio.domain.entity.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record SupplierRequestDto(
         Long supplierId,
         @NotBlank
         String document,
         @NotBlank
         String name,
         @Email
         String email,
         @NotBlank
         String cep,
        // @NotNull
        // Integer type,
         String rg,
         LocalDate birthDate,
         Set<Long> enterprises
) {
}
