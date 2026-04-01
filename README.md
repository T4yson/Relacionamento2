# Atividade 2 — Cliente e Pedido

API REST desenvolvida com Spring Boot para gerenciar o relacionamento entre clientes e pedidos, com foco em validação de relacionamento no cadastro.

---

## 📋 Cenário

Um **cliente** pode ter vários **pedidos**, mas um pedido pertence a apenas um cliente. O `Pedido` é o lado dono do relacionamento e carrega a chave estrangeira `cliente_id`. Antes de salvar um pedido, o cliente informado **precisa existir**.

---

## 🗂️ Estrutura do Projeto

```
atividade2/
├── controller/
│   ├── ClienteController.java
│   └── PedidoController.java
├── service/
│   ├── ClienteService.java
│   └── PedidoService.java
├── repository/
│   ├── ClienteRepository.java
│   └── PedidoRepository.java
├── model/
│   ├── Cliente.java
│   └── Pedido.java
└── dto/
    ├── Cliente/
    │   ├── ClienteRequest.java
    │   └── ClienteResponse.java
    └── Pedido/
        ├── PedidoRequest.java
        └── PedidoResponse.java
```

---

## 🔗 Relacionamento entre Entidades

```
Cliente (1) ────── (N) Pedido
```

| Entidade | Papel no relacionamento |
|---|---|
| `Cliente` | Lado inverso — possui `@OneToMany(mappedBy = "cliente")` |
| `Pedido` | Lado dono — possui `@ManyToOne` e a FK `cliente_id` |

---

## 📦 DTOs

### Cliente

| DTO | Campos | Uso |
|---|---|---|
| `ClienteRequest` | `nome`, `email` | Cadastro |
| `ClienteResponse` | `id`, `nome`, `email` | Listagem |

### Pedido

| DTO | Campos | Uso |
|---|---|---|
| `PedidoRequest` | `descricao`, `valor`, `clienteId` | Cadastro |
| `PedidoResponse` | `id`, `descricao`, `valor`, `clienteId`, `clienteNome` | Listagem e busca |

---

## 🛠️ Endpoints

### Clientes — `/atividade2/clientes`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/atividade2/clientes` | Cadastra um novo cliente |
| `GET` | `/atividade2/clientes` | Lista todos os clientes |

### Pedidos — `/atividade2/pedidos`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/atividade2/pedidos` | Cadastra um pedido vinculado a um cliente existente |
| `GET` | `/atividade2/pedidos` | Lista pedidos (com filtros opcionais) |
| `GET` | `/atividade2/pedidos/{id}` | Busca pedido por ID |
| `GET` | `/atividade2/pedidos/cliente/{clienteId}` | Lista pedidos de um cliente específico |
| `GET` | `/atividade2/pedidos/busca-especifica` | Busca pedido por ID e descrição |

#### Parâmetros de filtro — `GET /atividade2/pedidos`

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `clienteId` | `Long` | Filtra pedidos de um cliente específico |
| `nomeCliente` | `String` | Filtra pelo nome do cliente (parcial, case-insensitive) |

#### Parâmetros — `GET /atividade2/pedidos/cliente/{clienteId}`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `ordenado` | `boolean` | `false` | Quando `true`, retorna os pedidos ordenados por ID crescente |

#### Parâmetros — `GET /atividade2/pedidos/busca-especifica`

| Parâmetro | Tipo | Descrição |
|---|---|---|
| `id` | `Long` | ID do pedido |
| `descricao` | `String` | Descrição exata do pedido |

---

## 📝 Exemplos de Requisição

### Cadastrar Cliente
```http
POST /atividade2/clientes
Content-Type: application/json

{
  "nome": "Maria Oliveira",
  "email": "maria@email.com"
}
```

### Cadastrar Pedido
```http
POST /atividade2/pedidos
Content-Type: application/json

{
  "descricao": "Notebook Dell",
  "valor": 4500.00,
  "clienteId": 1
}
```

### Listar pedidos por nome do cliente
```http
GET /atividade2/pedidos?nomeCliente=maria
```

### Listar pedidos por ID do cliente
```http
GET /atividade2/pedidos?clienteId=1
```

### Listar pedidos de um cliente, ordenados por ID
```http
GET /atividade2/pedidos/cliente/1?ordenado=true
```

### Buscar pedido por ID e descrição
```http
GET /atividade2/pedidos/busca-especifica?id=1&descricao=Notebook Dell
```

**Resposta de pedido:**
```json
{
  "id": 1,
  "descricao": "Notebook Dell",
  "valor": 4500.00,
  "clienteId": 1,
  "clienteNome": "Maria Oliveira"
}
```

---

## ⚙️ Regras de Negócio

- Não é possível cadastrar um pedido para um cliente inexistente — o `PedidoService` valida a existência do cliente antes de salvar.
- O DTO de entrada do pedido recebe apenas o `clienteId`, nunca o objeto completo.
- A listagem geral de pedidos prioriza o filtro por `clienteId` sobre o filtro por `nomeCliente`.
- O endpoint `/cliente/{clienteId}` também valida a existência do cliente antes de buscar os pedidos.
- A busca por `busca-especifica` retorna erro caso nenhum pedido corresponda à combinação de `id` + `descricao`.

---

## 🔍 Métodos do Repository

### `PedidoRepository`

```java
List<Pedido> findByClienteId(Long clienteId);
List<Pedido> findByClienteIdOrderByIdAsc(Long clienteId);
List<Pedido> findByClienteNomeContainingIgnoreCase(String nome);
Optional<Pedido> findByIdAndDescricao(Long id, String descricao);
```

---

## ✅ Validações dos DTOs

### `ClienteRequest`
| Campo | Validação |
|---|---|
| `nome` | `@NotBlank` |
| `email` | `@NotBlank` + `@Email` |

### `PedidoRequest`
| Campo | Validação |
|---|---|
| `descricao` | `@NotBlank` |
| `valor` | `@NotNull` + `@DecimalMin("0.0", inclusive = false)` |
| `clienteId` | `@NotNull` |

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- Lombok
- Jakarta Validation (Bean Validation)
- Banco de dados relacional (H2 / PostgreSQL / MySQL)
