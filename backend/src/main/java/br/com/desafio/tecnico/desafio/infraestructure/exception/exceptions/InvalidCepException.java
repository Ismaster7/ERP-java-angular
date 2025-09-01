package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class InvalidCepException extends RuntimeException {
    public InvalidCepException(){
        super("Endereço de cep não corresponde a lugar nenhum.");
    }
}
