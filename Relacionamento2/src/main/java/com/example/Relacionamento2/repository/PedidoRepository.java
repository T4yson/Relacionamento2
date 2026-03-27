package com.example.Relacionamento2.repository;

import com.example.Relacionamento2.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByClienteIdOrderByIdAsc(Long clienteId);

    List<Pedido> findByClienteNomeContainingIgnoreCase(String nome);

    Optional<Pedido> findByIdAndDescricao(Long id, String descricao);
}
