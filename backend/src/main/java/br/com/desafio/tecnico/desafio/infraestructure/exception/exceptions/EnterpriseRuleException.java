package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class EnterpriseRuleException extends RuntimeException {
    public EnterpriseRuleException(){
        super("Não é permitido cadastrar fornecedor pessoa física menor de idade para empresas do Paraná");
    }
    public EnterpriseRuleException(String message){
        super(message);
    }
}
