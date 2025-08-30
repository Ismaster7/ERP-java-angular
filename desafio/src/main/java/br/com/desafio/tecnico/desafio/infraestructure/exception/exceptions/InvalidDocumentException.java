package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class InvalidDocumentException extends RuntimeException{
    public InvalidDocumentException(String message){
        super(message);
    }
    public InvalidDocumentException(){
        super("Documento inválido informado");
    }
}
