package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class EnterpriseNotFound extends RuntimeException{
    public EnterpriseNotFound(String message){
        super(message);
    }

    public EnterpriseNotFound(){
        super("Empresa não encontrada");
    };
}
