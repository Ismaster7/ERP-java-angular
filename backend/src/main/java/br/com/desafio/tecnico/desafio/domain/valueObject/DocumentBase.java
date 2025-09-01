package br.com.desafio.tecnico.desafio.domain.valueObject;

public abstract class DocumentBase {


    public DocumentBase(){

    }

    public abstract boolean isValid();

    public boolean isValidCnpj(String cnpj) {
        return cnpj != null && cnpj.replaceAll("\\D", "").length() == 14;
    }


}

