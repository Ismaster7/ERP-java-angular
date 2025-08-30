package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class EnterpriseNotFoundException extends RuntimeException{
    public EnterpriseNotFoundException(String message){
        super(message);
    }

    public EnterpriseNotFoundException(){
        super("Empresa não encontrada");
    };
}
