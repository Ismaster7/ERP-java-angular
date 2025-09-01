package br.com.desafio.tecnico.desafio.infraestructure.exception;

import java.time.LocalDateTime;

public record ExceptionTemplate(
        String exeption,
        String details,
        LocalDateTime timeStamp
) {
}
