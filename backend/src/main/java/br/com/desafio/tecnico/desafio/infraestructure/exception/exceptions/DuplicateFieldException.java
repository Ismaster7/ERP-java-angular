package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String fieldName) {
        super("Já existe um registro com o valor do campo único: " + fieldName);
    }

    public DuplicateFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
