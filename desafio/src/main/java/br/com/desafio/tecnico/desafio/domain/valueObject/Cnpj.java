package br.com.desafio.tecnico.desafio.domain.valueObject;

import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.InvalidDocumentException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Cnpj extends DocumentBase{
    @Column(name = "cnpj", unique = true)
    private String document;

    public Cnpj() {
    }

    public Cnpj(String cnpj) {
        this.document = cnpj;
        if(isValid()){
            this.document = document.replaceAll("\\D", "");
        }else{
            throw new InvalidDocumentException("CNPJ inválido!");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }

    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean isValid() {
        return isValidCnpj(document);
    }
}
