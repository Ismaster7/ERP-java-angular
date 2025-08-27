package br.com.desafio.tecnico.desafio.infraestructure.exception;

import br.com.desafio.tecnico.desafio.infraestructure.exception.exceptions.EnterpriseNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EnterpriseNotFound.class)
    public ResponseEntity<ExceptionTemplate> handleEntityNotFound(EnterpriseNotFound e, WebRequest request) {
        var template = new ExceptionTemplate(
                e.getMessage(),
                request.getDescription(false),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
        );
        return new ResponseEntity<>(template, NOT_FOUND);
    }

}