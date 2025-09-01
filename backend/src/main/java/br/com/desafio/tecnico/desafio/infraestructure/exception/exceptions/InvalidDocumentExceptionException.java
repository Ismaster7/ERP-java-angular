package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class InvalidDocumentExceptionException extends RuntimeException{
    public InvalidDocumentExceptionException(String message){
        super(message);
    }
    public InvalidDocumentExceptionException(){
        super("Documento inválido informado");
    }
}
