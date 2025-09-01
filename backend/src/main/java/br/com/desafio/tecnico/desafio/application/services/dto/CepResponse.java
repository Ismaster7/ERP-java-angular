package br.com.desafio.tecnico.desafio.application.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record CepResponse(

        @JsonIgnoreProperties(ignoreUnknown = true)
        String cep,
        String estado,
        String uf

) {
}
