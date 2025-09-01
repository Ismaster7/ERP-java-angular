package br.com.desafio.tecnico.desafio.domain.entity.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record SupplierRequestUpdateDto(
         @NotNull
         Long supplierId,
         String document,
         String name,
         @Email
         String email,
         String cep,
         String rg,
         LocalDate birthDate,
         Set<Long> enterprises
) {
}
