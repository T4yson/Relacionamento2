package com.example.Relacionamento2.controller;


import com.example.Relacionamento2.dto.Pedido.PedidoRequest;
import com.example.Relacionamento2.dto.Pedido.PedidoResponse;
import com.example.Relacionamento2.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividade2/pedidos")
@RequiredArgsConstructor
public class PedidoCOntroller {

    private final PedidoService pedidoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criarPedido(@Valid @RequestBody PedidoRequest request) {
        return pedidoService.criarPedido(request);
    }

    @GetMapping
    public List<PedidoResponse> listar(
            @RequestParam(required = false) String nomeCliente,
            @RequestParam(required = false) Long clienteId
    ) {
        return pedidoService.listar(nomeCliente, clienteId);
    }

    @GetMapping("/{id}")
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<PedidoResponse> buscarPorCliente(

            @PathVariable Long clienteId,
            @RequestParam(defaultValue = "false") boolean ordenado
    ) {
        return pedidoService.buscarPorCLiente(clienteId, ordenado);
    }

    @GetMapping("/busca-especifica")
    public PedidoResponse buscarPorIdAndDescricao (
            @RequestParam Long id,
            @RequestParam String descricao
    ) {
        return pedidoService.buscarPorIdAndDescricao(id, descricao);
    }
}
