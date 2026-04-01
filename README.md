<h1 align="center">Relacionamento2 — API REST: Cliente & Pedido</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen?style=flat-square&logo=springboot" />
  <img src="https://img.shields.io/badge/H2-Database-blue?style=flat-square" />
  <img src="https://img.shields.io/badge/Lombok-enabled-red?style=flat-square" />
  <img src="https://img.shields.io/badge/Maven-build-C71A36?style=flat-square&logo=apachemaven" />
</p>

> API REST construída com **Spring Boot 4** para gerenciar o relacionamento **um-para-muitos** entre **Clientes** e **Pedidos**, explorando boas práticas de arquitetura em camadas, validação de entrada, DTOs com Java Records e consultas derivadas via Spring Data JPA.

---

## 📑 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Stack Tecnológica](#-stack-tecnológica)
- [Arquitetura e Estrutura](#-arquitetura-e-estrutura)
- [Modelagem de Dados](#-modelagem-de-dados)
- [DTOs — Contratos de Entrada e Saída](#-dtos--contratos-de-entrada-e-saída)
- [Endpoints da API](#-endpoints-da-api)
- [Regras de Negócio](#-regras-de-negócio)
- [Validações](#-validações)
- [Decisões Técnicas](#-decisões-técnicas)
- [Como Executar](#-como-executar)
- [Exemplos de Requisição](#-exemplos-de-requisição)
- [Autor](#-autor)

---

## 💡 Sobre o Projeto

Este projeto é uma atividade acadêmica que implementa uma API REST completa para demonstrar o relacionamento bidirecional `@OneToMany` / `@ManyToOne` entre as entidades **Cliente** e **Pedido**. O foco está em:

- Validação de integridade referencial na camada de serviço (não apenas no banco).
- Separação de responsabilidades com o padrão **Controller → Service → Repository**.
- Uso de **Java Records** como DTOs imutáveis.
- Consultas flexíveis com **derived query methods** do Spring Data JPA.

---

## 🛠 Stack Tecnológica

| Tecnologia | Versão | Função |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.0.5 | Framework base |
| Spring Web MVC | — | Camada HTTP / REST |
| Spring Data JPA | — | Persistência e ORM |
| H2 Database | — | Banco de dados em memória (desenvolvimento) |
| Lombok | — | Redução de boilerplate (getters, construtores, etc.) |
| Jakarta Validation | — | Bean Validation nos DTOs de entrada |
| Spring Boot DevTools | — | Hot reload em desenvolvimento |
| Maven | — | Gerenciador de build e dependências |

---

## 🗂 Arquitetura e Estrutura

O projeto segue uma **arquitetura em camadas** clássica para APIs REST com Spring Boot:

```
src/main/java/com/example/Relacionamento2/
│
├── controller/                  # Camada HTTP — recebe e responde requisições
│   ├── ClienteController.java
│   └── PedidoCOntroller.java
│
├── service/                     # Camada de negócio — orquestra a lógica e validações
│   ├── ClienteService.java
│   └── PedidoService.java
│
├── repository/                  # Camada de dados — interface com o banco via Spring Data JPA
│   ├── ClienteRepository.java
│   └── PedidoRepository.java
│
├── model/                       # Entidades JPA mapeadas para o banco de dados
│   ├── Cliente.java
│   └── Pedido.java
│
├── dto/                         # Data Transfer Objects (Java Records)
│   ├── Cliente/
│   │   ├── ClienteRequest.java
│   │   └── ClienteResponse.java
│   └── Pedido/
│       ├── PedidoRequest.java
│       └── PedidoResponse.java
│
└── Relacionamento2Application.java   # Entry point da aplicação
```

> **Fluxo de uma requisição:**
> `HTTP Request → Controller (@RestController) → Service (@Service) → Repository (JpaRepository) → Banco (H2)`

---

## 🔗 Modelagem de Dados

```
┌─────────────────────┐          ┌──────────────────────────┐
│       Cliente        │  1    N  │         Pedido            │
│─────────────────────│──────────│──────────────────────────│
│ id (PK)             │          │ id (PK)                   │
│ nome                │          │ descricao                 │
│ email               │          │ valor (BigDecimal)        │
│                     │          │ cliente_id (FK)           │
└─────────────────────┘          └──────────────────────────┘
```

| Entidade | Anotação JPA | Papel |
|---|---|---|
| `Cliente` | `@OneToMany(mappedBy = "Cliente", cascade = CascadeType.ALL)` | Lado inverso do relacionamento |
| `Pedido` | `@JoinColumn(name = "cliente_id", nullable = false)` | Lado dono — carrega a chave estrangeira |

- **`CascadeType.ALL`** no `Cliente`: operações de persistência propagam para os pedidos filhos.
- **`FetchType.LAZY`** no `Pedido`: o `Cliente` associado só é carregado da base quando acessado, otimizando consultas.

---

## 📦 DTOs — Contratos de Entrada e Saída

Todos os DTOs são implementados como **Java Records**, garantindo imutabilidade e eliminando boilerplate.

### Cliente

| DTO | Campos | Uso |
|---|---|---|
| `ClienteRequest` | `nome`, `email` | Body do `POST /clientes` |
| `ClienteResponse` | `id`, `nome`, `email` | Resposta de listagem e criação |

### Pedido

| DTO | Campos | Uso |
|---|---|---|
| `PedidoRequest` | `descricao`, `valor`, `clienteId` | Body do `POST /pedidos` |
| `PedidoResponse` | `id`, `descricao`, `valor`, `clienteId`, `clienteNome` | Resposta de todas as consultas |

> O `PedidoRequest` recebe apenas o `clienteId` (nunca o objeto `Cliente` completo), mantendo o desacoplamento entre camadas. O `PedidoResponse` enriquece a saída com o `clienteNome` para evitar uma chamada extra por parte do consumidor.

---

## 🛠️ Endpoints da API

### Base URL: `http://localhost:8080`

---

### Clientes — `/atividade2/clientes`

| Método | Rota | Status | Descrição |
|---|---|---|---|
| `POST` | `/atividade2/clientes` | `201 Created` | Cadastra um novo cliente |
| `GET` | `/atividade2/clientes` | `200 OK` | Lista todos os clientes |

---

### Pedidos — `/atividade2/pedidos`

| Método | Rota | Status | Descrição |
|---|---|---|---|
| `POST` | `/atividade2/pedidos` | `201 Created` | Cadastra um pedido vinculado a um cliente existente |
| `GET` | `/atividade2/pedidos` | `200 OK` | Lista pedidos com filtros opcionais |
| `GET` | `/atividade2/pedidos/{id}` | `200 OK` | Busca pedido pelo seu ID |
| `GET` | `/atividade2/pedidos/cliente/{clienteId}` | `200 OK` | Lista pedidos de um cliente específico |
| `GET` | `/atividade2/pedidos/busca-especifica` | `200 OK` | Busca pedido por ID + descrição exata |

#### Query Params — `GET /atividade2/pedidos`

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `clienteId` | `Long` | Não | Filtra pedidos pelo ID do cliente |
| `nomeCliente` | `String` | Não | Filtra pelo nome do cliente (parcial, case-insensitive) |

> **Prioridade de filtro:** `clienteId` tem precedência sobre `nomeCliente`. Se nenhum filtro for informado, retorna todos os pedidos.

#### Query Params — `GET /atividade2/pedidos/cliente/{clienteId}`

| Parâmetro | Tipo | Default | Descrição |
|---|---|---|---|
| `ordenado` | `boolean` | `false` | Quando `true`, retorna pedidos ordenados por ID crescente |

#### Query Params — `GET /atividade2/pedidos/busca-especifica`

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `id` | `Long` | Sim | ID do pedido |
| `descricao` | `String` | Sim | Descrição exata do pedido |

---

## ⚙️ Regras de Negócio

1. **Validação de cliente ao criar pedido:** O `PedidoService` chama `ClienteService.findEntityById()` antes de persistir o pedido. Se o cliente não existir, uma `RuntimeException` é lançada e o pedido não é salvo.

2. **Validação de cliente ao listar pedidos por cliente:** O endpoint `/cliente/{clienteId}` também valida a existência do cliente antes de retornar os pedidos.

3. **Prioridade nos filtros de listagem:** O serviço avalia `clienteId` primeiro; só avalia `nomeCliente` se `clienteId` for nulo. Sem parâmetros, retorna todos.

4. **Busca combinada obrigatória:** A `busca-especifica` exige que o pedido corresponda simultaneamente ao `id` **e** à `descricao` informados, caso contrário retorna erro.

5. **Isolamento de representação:** As entidades JPA nunca trafegam diretamente na API; toda entrada e saída passa pelos DTOs, protegendo o modelo de domínio.

---

## ✅ Validações

### `ClienteRequest`

| Campo | Anotação | Mensagem de Erro |
|---|---|---|
| `nome` | `@NotBlank` | `"Nome do cliente e obrigatorio"` |
| `email` | `@NotBlank` + `@Email` | `"Email do cliente e obrigatorio"` / `"Email invalido"` |

### `PedidoRequest`

| Campo | Anotação | Mensagem de Erro |
|---|---|---|
| `descricao` | `@NotBlank` | `"Descricao do pedido e obrigatoria"` |
| `valor` | `@NotNull` + `@DecimalMin("0.0", inclusive = false)` | `"Valor do pedido e obrigatorio"` / `"Valor deve ser maior que zero"` |
| `clienteId` | `@NotNull` | `"Cliente e obrigatorio"` |

> As validações são ativadas pelo `@Valid` nos métodos de controller, delegando ao Jakarta Bean Validation (Hibernate Validator).

---

## 🧠 Decisões Técnicas

| Decisão | Justificativa |
|---|---|
| **Java Records para DTOs** | Imutabilidade por padrão, sintaxe enxuta, sem necessidade de Lombok nos contratos de API |
| **Lombok nas entidades** | `@Data`, `@AllArgsConstructor` e `@NoArgsConstructor` eliminam boilerplate nos modelos JPA |
| **`@RequiredArgsConstructor` nos serviços e controllers** | Permite injeção de dependência via construtor sem `@Autowired`, favorecendo testabilidade |
| **Derived Query Methods no `PedidoRepository`** | Evita JPQL manual para consultas simples; o Spring Data JPA infere o SQL pelo nome do método |
| **H2 em memória** | Banco embarcado ideal para desenvolvimento e testes sem infraestrutura externa |
| **`CascadeType.ALL` no Cliente** | Garante que pedidos órfãos sejam gerenciados automaticamente junto ao ciclo de vida do cliente |
| **`FetchType.LAZY` no Pedido** | Evita carregamento desnecessário do cliente em consultas que não precisam desse dado |
| **Validação de existência na camada de serviço** | A regra de integridade é protegida independentemente do banco, sendo explícita e testável |

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 21+** instalado
- **Maven 3.8+** instalado (ou use o wrapper incluso `./mvnw`)

### Passos

```bash
# 1. Clone o repositório
git clone https://github.com/T4yson/Relacionamento2.git
cd Relacionamento2/Relacionamento2

# 2. Execute a aplicação
./mvnw spring-boot:run
# ou no Windows:
mvnw.cmd spring-boot:run
```

A API estará disponível em: **`http://localhost:8080`**

> O banco H2 é inicializado automaticamente em memória. Nenhuma configuração adicional de banco de dados é necessária.

---

## 📝 Exemplos de Requisição

### ✅ Cadastrar Cliente

```http
POST /atividade2/clientes
Content-Type: application/json

{
  "nome": "Maria Oliveira",
  "email": "maria@email.com"
}
```

**Resposta `201 Created`:**
```json
{
  "id": 1,
  "nome": "Maria Oliveira",
  "email": "maria@email.com"
}
```

---

### ✅ Cadastrar Pedido

```http
POST /atividade2/pedidos
Content-Type: application/json

{
  "descricao": "Notebook Dell",
  "valor": 4500.00,
  "clienteId": 1
}
```

**Resposta `201 Created`:**
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

### 🔍 Listar pedidos com filtros

```http
# Filtrar por ID do cliente
GET /atividade2/pedidos?clienteId=1

# Filtrar por nome parcial do cliente (case-insensitive)
GET /atividade2/pedidos?nomeCliente=maria

# Listar todos os pedidos
GET /atividade2/pedidos
```

---

### 🔍 Listar pedidos de um cliente, ordenados por ID

```http
GET /atividade2/pedidos/cliente/1?ordenado=true
```

---

### 🔍 Buscar pedido por ID e descrição exata

```http
GET /atividade2/pedidos/busca-especifica?id=1&descricao=Notebook Dell
```

---

## 👤 Autor

<table>
  <tr>
    <td align="center">
      <b>Igor Tayson Bresolin Savero</b><br/>
      <a href="https://github.com/T4yson">@T4yson</a><br/>
      <a href="mailto:igor_savero@estudante.sesisenai.org.br">igor_savero@estudante.sesisenai.org.br</a>
    </td>
  </tr>
</table>

---

<p align="center">Feito com ☕ e Spring Boot</p>
