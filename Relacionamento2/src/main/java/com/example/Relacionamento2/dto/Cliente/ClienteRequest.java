package com.example.Relacionamento2.dto.Cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest (
        @NotBlank(message = "Nome do cliente e obrigatorio")
        String nome,
        @Email(message = "Email invalido")
        @NotBlank(message = "Email do cliente e obrigatorio")
        String email
) {
}
