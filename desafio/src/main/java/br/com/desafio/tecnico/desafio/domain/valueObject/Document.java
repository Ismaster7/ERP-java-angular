package br.com.desafio.tecnico.desafio.domain.valueObject;

import jakarta.persistence.Embeddable;

@Embeddable
public class Document {

    private String document;

    protected Document() {}

    private Document(String document) {
        if (!isValidCpf(document) && !isValidCnpj(document)) {

            throw new IllegalArgumentException("CPf ou Cnpj inválido");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }
        this.document = document.replaceAll("\\D", "");
    }

    public static Document of(String value) {
        return new Document(value);
    }

    public boolean isCpf() {
        return document.length() == 11;
    }

    public boolean isCnpj() {
        return document.length() == 14;
    }

    public String getDocument() {
        return document;
    }

    private boolean isValidCpf(String cpf) {
        // implementação clássica do cálculo de dígitos verificadores
        return cpf != null && cpf.replaceAll("\\D", "").length() == 11;
    }

    private boolean isValidCnpj(String cnpj) {
        return cnpj != null && cnpj.replaceAll("\\D", "").length() == 14;
    }
}
