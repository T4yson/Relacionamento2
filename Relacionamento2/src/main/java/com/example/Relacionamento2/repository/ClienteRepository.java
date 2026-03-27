package com.example.Relacionamento2.repository;

import com.example.Relacionamento2.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
