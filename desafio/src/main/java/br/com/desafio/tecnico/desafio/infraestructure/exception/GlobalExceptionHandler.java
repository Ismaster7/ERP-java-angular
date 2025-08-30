package br.com.desafio.tecnico.desafio.infraestructure.exception;

import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionTemplate> generalHandle(Exception e, WebRequest request) {
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, NOT_FOUND);
    }

    @ExceptionHandler(EnterpriseNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> handleEntityNotFound(EnterpriseNotFoundException e, WebRequest request) {
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, NOT_FOUND);
    }

    @ExceptionHandler(InvalidDocumentExceptionException.class)
    public ResponseEntity<ExceptionTemplate> invalidDocumentHandle(InvalidDocumentExceptionException e, WebRequest request){
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, BAD_REQUEST);
    }
    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<ExceptionTemplate> supplierNotFoundHandle(SupplierNotFoundException e, WebRequest request){
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, NOT_FOUND);
    }

    @ExceptionHandler(InvalidCepException.class)
    public ResponseEntity<ExceptionTemplate> wrongCepHandle(InvalidCepException e, WebRequest request){
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, NOT_FOUND);
    }
    @ExceptionHandler(EnterpriseRuleException.class)
    public ResponseEntity<ExceptionTemplate> enterpriseRuleHandle(EnterpriseRuleException e, WebRequest request){
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, BAD_REQUEST);
    }

}