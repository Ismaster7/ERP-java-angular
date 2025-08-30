package br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions;

public class SupplierNotFoundException extends RuntimeException{
    public SupplierNotFoundException(String message){
        super(message);
    }
    public SupplierNotFoundException(){
        super("Supplier não encontrado");
    }
}
