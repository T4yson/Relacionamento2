package com.example.Relacionamento2.controller;

import com.example.Relacionamento2.dto.Cliente.ClienteRequest;
import com.example.Relacionamento2.dto.Cliente.ClienteResponse;
import com.example.Relacionamento2.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividade2/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criarCliente(@Valid @RequestBody ClienteRequest request) {
        return clienteService.criarCliente(request);
    }

    @GetMapping
    public List<ClienteResponse> listar(){
        return clienteService.listar();
    }
}
