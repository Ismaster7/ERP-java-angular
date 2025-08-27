package br.com.desafio.tecnico.desafio.domain.dto;

import br.com.desafio.tecnico.desafio.domain.entity.Supplier;
import br.com.desafio.tecnico.desafio.domain.valueObject.Cep;
import br.com.desafio.tecnico.desafio.domain.valueObject.Document;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public record EnterpriseRequestDto(

        Long enterpriseId,
        @NotBlank

        Document cnpj,

        String tradeName,

        Cep cep,

        Set<Supplier> suppliers
) {
}
