package com.pacifico.reto.tecnico.dto;

public record CustomerResponse(

        Long id,

        String firstName,

        String lastName,

        String email,

        String phone,

        Boolean active

) {
}
