package com.example.Relacionamento2.dto.Pedido;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PedidoRequest (
        @NotBlank(message = "Descricao do pedido e obrigatoria")
        String descricao,
        @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
        @NotNull(message = "Valor do pedido e obrigatorio")
        BigDecimal valor,
        @NotNull(message = "Cliente e obrigatorio")
        Long clienteId
) {
}
