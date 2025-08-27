package br.com.desafio.tecnico.desafio.application.services.dto;

public record CepResponse(

     String cep,
     String uf,
     String cidade,
     String bairro,
     String logradouro

    // Getters e Setters

) {
}
