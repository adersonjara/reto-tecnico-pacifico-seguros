package com.pacifico.reto.tecnico.dto;

import java.time.LocalDateTime;

public record ErrorResponse(

        String message,

        LocalDateTime timestamp

) {
}