package br.com.desafio.tecnico.desafio.domain.dto;

import br.com.desafio.tecnico.desafio.domain.enums.Tipo;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public record FornecedorRequestDto(

        Long id,
        @NotBlank
        @Pattern(regexp = "\\d{11}|\\d{14}", message = "Documento deve ter 11 (CPF) ou 14 (CNPJ) dígitos")
        String documento,
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 8, message = "CEP deve ter 8 dígitos")
        String cep,

        @NotNull
        Tipo tipo,

// Apenas para pessoa física
        String rg,

        @Past
        LocalDate dataNascimento,

        Set<Long> empresaIds
) {

}
