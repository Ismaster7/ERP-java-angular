package br.com.desafio.tecnico.desafio.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaRequestDto(
     Long id,

    @NotBlank
    @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos")
     String cnpj,

    @NotBlank
     String nomeFantasia,

    @NotBlank
    @Size(min = 8, max = 8, message = "CEP deve ter 8 dígitos")
     String cep
    ){}

