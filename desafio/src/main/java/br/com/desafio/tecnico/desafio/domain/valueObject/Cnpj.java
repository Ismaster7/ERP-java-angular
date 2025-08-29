package br.com.desafio.tecnico.desafio.domain.valueObject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cnpj extends DocumentBase{
    @Column(name = "cnpj", unique = true)
    private String document;

    public Cnpj() {
    }

    public Cnpj(String cnpj) {
        if(this.isValid()){
            this.document = cnpj;
            this.document = document.replaceAll("\\D", "");
        }else{
            throw new IllegalArgumentException("CNPJ inválido!");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }

    }

    @Override
    public boolean isValid() {
        return isValidCnpj(document);
    }
}
