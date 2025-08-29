package br.com.desafio.tecnico.desafio.domain.valueObject;

import jakarta.persistence.Embeddable;

@Embeddable
public class Cep {

    private String cepValue;

    protected Cep() {}

    public Cep(String cepValue) {
        if (!isValidCep(cepValue)) {
            throw new IllegalArgumentException("Cep Inválido");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }
        this.cepValue = cepValue;
    }

    public static Cep of(String cepValue) {
        return new Cep(cepValue);
    }

    public String getValue() {
        return cepValue;
    }

    private boolean isValidCep(String cep) {
        return cep != null && cep.matches("\\d{5}-?\\d{3}");
    }
}
