package br.com.desafio.tecnico.desafio.domain.valueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Document extends DocumentBase {

    @Column(name = "document", unique = true)
    private String document;

    protected Document() {}

    public Document(String document) {
        this.document = document;
        if(this.isValid()){
            this.document = document.replaceAll("\\D", "");
        }else{
            throw new IllegalArgumentException("CPf ou Cnpj inválido");
            /* criei este validador no construtor para lógicas de negócio interno.
            para validação da chegada da requisição, usei o @Validation do Spring mesmo.
             */
        }
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
    @Override
    public String toString(){
        return this.getDocument();
    }



    @Override
    public boolean isValid() {
        if (!isValidCpf(document) && !isValidCnpj(document)) {
            return false;
        }
        return true;
    }
}
