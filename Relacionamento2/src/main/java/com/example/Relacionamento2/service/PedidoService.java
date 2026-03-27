package com.example.Relacionamento2.service;

import com.example.Relacionamento2.dto.Pedido.PedidoRequest;
import com.example.Relacionamento2.dto.Pedido.PedidoResponse;
import com.example.Relacionamento2.model.Cliente;
import com.example.Relacionamento2.model.Pedido;
import com.example.Relacionamento2.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;

    public PedidoResponse criarPedido(PedidoRequest request) {
        Cliente cliente = clienteService.findEntityById(request.clienteId());
        Pedido pedido = new Pedido();
        pedido.setDescricao(request.descricao());
        pedido.setValor(request.valor());
        pedido.setCliente(cliente);
        return toResponse(pedidoRepository.save(pedido));
    }

    public List<PedidoResponse> listar(String nomeCLiente, Long clienteId) {
        if (clienteId != null) {
            return pedidoRepository.findByClienteId(clienteId).stream().map(this::toResponse).toList();
        }
        if (nomeCLiente != null && !nomeCLiente.isBlank()) {
            return pedidoRepository.findByClienteNomeContainingIgnoreCase(nomeCLiente).stream().map(this::toResponse).toList();
        }
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PedidoResponse buscarPorId(Long id) {
        return pedidoRepository.findById(id).map(this::toResponse).orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    }

    public List<PedidoResponse> buscarPorCLiente(Long clienteId, boolean ordenarPorId) {
        clienteService.findEntityById(clienteId);
        List<Pedido> pedidos = ordenarPorId
                ? pedidoRepository.findByClienteIdOrderByIdAsc(clienteId)
                : pedidoRepository.findByClienteId(clienteId);
        return pedidos.stream().map(this::toResponse).toList();
    }

    public  PedidoResponse buscarPorIdAndDescricao(Long id, String descricao) {
        return pedidoRepository.findByIdAndDescricao(id, descricao)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado para os filtros informados"));
    }

    private PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getDescricao(),
                pedido.getValor(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome()
        );
    }
}
