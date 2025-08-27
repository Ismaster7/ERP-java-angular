package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

import br.com.desafio.tecnico.desafio.domain.entity.Enterprise;

public class EnterpriseNotFound extends RuntimeException{
    public EnterpriseNotFound(String message){
        super(message);
    }

    public EnterpriseNotFound(){
        super("Empresa não encontrada");
    };
}
