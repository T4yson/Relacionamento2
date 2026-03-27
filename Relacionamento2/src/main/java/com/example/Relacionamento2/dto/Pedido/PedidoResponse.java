package com.example.Relacionamento2.dto.Pedido;

import java.math.BigDecimal;

public record PedidoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        Long clienteId,
        String clienteNome
) {
}
