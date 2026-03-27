package com.example.Relacionamento2.service;

import com.example.Relacionamento2.dto.Cliente.ClienteRequest;
import com.example.Relacionamento2.dto.Cliente.ClienteResponse;
import com.example.Relacionamento2.model.Cliente;
import com.example.Relacionamento2.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteResponse criarCliente(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        return toResponse(clienteRepository.save(cliente));
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Cliente findEntityById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CLiente não encontrado: " + id));
    }

    private ClienteResponse toResponse(Cliente cLiente) {
        return new ClienteResponse(cLiente.getId(), cLiente.getNome(), cLiente.getEmail());
    }
}
