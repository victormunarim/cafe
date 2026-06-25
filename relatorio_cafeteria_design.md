# Análise de Design de Código: Sistema Cafeteria

Este relatório analisa o design de código do seu sistema de cafeteria atual, destacando onde as boas práticas (Baixo Acoplamento, Ocultação de Informação, Composição, Lei de Demeter e SOLID) foram aplicadas e onde podem ser melhoradas.

---

## 1. Baixo Acoplamento (Low Coupling)
No seu código, o acoplamento fraco é obtido através do uso de **Interfaces** para a camada de persistência.

* **Onde está aplicado:** 
  A classe [PedidoService](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/service/PedidoService.java) depende de abstrações (`PedidoRepository` e `ClienteRepository`), e não das classes concretas em memória.
  ```java
  public class PedidoService {
      private final PedidoRepository pedidoRepository; // Interface (Baixo acoplamento)
      private final ClienteRepository clienteRepository; // Interface (Baixo acoplamento)

      public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
          this.pedidoRepository = pedidoRepository;
          this.clienteRepository = clienteRepository;
      }
  }
  ```
* **Vantagem:** Se futuramente você quiser salvar os pedidos em um banco de dados real (usando PostgreSQL com JDBC ou Spring Data JPA), basta criar uma classe `SqlPedidoRepository implements PedidoRepository`. Você **não precisará alterar uma única linha de código** em `PedidoService` ou em `App`.

---

## 2. Ocultação de Informação (Information Hiding / Encapsulamento)
Refere-se a proteger o estado interno do objeto contra manipulações externas inválidas, expondo apenas o necessário.

* **Onde está aplicado:**
  Na classe [Produto](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/Produto.java), os atributos são `private` e o construtor/setters garantem que as regras básicas não sejam burladas. 
  No seu [ProdutoService](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/service/ProdutoService.java), temos validações de regras de negócio antes de realizar operações de escrita:
  ```java
  public class ProdutoService {
      public Produto cadastrar(Produto produto) {
          if (produto == null) {
              throw new IllegalArgumentException("Produto não pode ser nulo");
          }
          if (produto.getPreco() < 0) { // Proteção/Validação de estado
              throw new IllegalArgumentException("Preço não pode ser negativo");
          }
          return produtoRepository.salvar(produto);
      }
  }
  ```
  Isso garante que um produto nunca entre no sistema com preço inválido (negativo).

---

## 3. Preferência por Composição em Vez de Herança
A composição estabelece uma relação de "tem um" (um objeto contém outro), tornando o design flexível e dinâmico.

* **Onde está aplicado:**
  Na classe [Pedido](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/Pedido.java), a composição é usada para associar um `Pedido` a um `Cliente` e a uma lista de `ItemPedido`:
  ```java
  public class Pedido {
      private int id;
      private Cliente cliente; // Composição: Pedido "tem um" Cliente
      private List<ItemPedido> itens = new ArrayList<>(); // Composição: Pedido "tem" Itens
      private double valorTotal;
  }
  ```
  Evitou-se a herança. Seria um erro de design fazer `Pedido extends Cliente`, pois um Pedido não "é um" Cliente. Com a composição, as entidades permanecem independentes e flexíveis.

---

## 4. Lei de Demeter (Princípio do Menor Conhecimento)
Uma classe deve interagir apenas com seus colaboradores imediatos. Ela não deve "navegar" por vários níveis de dependência (o antipattern *train wreck*).

* **Onde há uma oportunidade de melhoria no seu código:**
  No seu [App.java](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/App.java) (ao exibir o resumo do pedido), você tem a linha:
  ```java
  IO.println("Cliente: " + pedido.getCliente().getNome()); // Violar Demeter (App navega até o nome do cliente através de Pedido)
  ```
  Também no loop de itens do pedido:
  ```java
  System.out.printf("  %s (x%d) - R$ %.2f\n",
          item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal()); // Violar Demeter (navega item -> produto -> nome)
  ```
* **Como corrigir seguindo a Lei de Demeter:**
  Podemos criar métodos delegados nos próprios objetos para que a chamada externa não precise conhecer a estrutura interna do objeto vizinho:
  
  No [Pedido](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/Pedido.java):
  ```java
  public String getNomeCliente() {
      return this.cliente != null ? this.cliente.getNome() : "Desconhecido";
  }
  ```
  No [ItemPedido](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/ItemPedido.java):
  ```java
  public String getNomeProduto() {
      return this.produto != null ? this.produto.getNome() : "Produto Inválido";
  }
  ```
  Dessa forma, no `App.java`, você chamaria apenas:
  ```java
  IO.println("Cliente: " + pedido.getNomeCliente()); // Limpo e seguro contra NullPointerException!
  ```

---

## 5. Princípios SOLID no seu Código

### S - Single Responsibility Principle (Responsabilidade Única)
Seu projeto está bem estruturado com responsabilidades divididas de forma clara:
* **Models ([Cliente](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/Cliente.java), [Pedido](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/model/Pedido.java)):** Apenas mantêm dados e lógicas internas puras (como cálculo do total).
* **Repositories ([ClienteRepository](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/repository/ClienteRepository.java)):** Apenas definem o contrato de persistência.
* **Services ([PedidoService](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/service/PedidoService.java)):** Apenas executam fluxos de casos de uso e regras de negócio.
* **App ([App](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/App.java)):** Apenas gerencia a interface com o usuário (CLI).

---

### O - Open/Closed Principle (Aberto/Fechado)
As interfaces de repositório deixam o sistema aberto para receber novos tipos de persistência sem precisar modificar o código core.
* Se você precisar criar um banco de dados em arquivo JSON, basta criar uma classe `JsonPedidoRepository implements PedidoRepository` e injetá-la. A classe `PedidoService` estará **fechada para modificação** e continuará funcionando.

---

### L - Liskov Substitution Principle (Substituição de Liskov)
* **Onde está aplicado:**
  A classe [InMemoryPedidoRepository](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/repository/InMemoryPedidoRepository.java) implementa a interface `PedidoRepository`. 
  Qualquer classe que espera receber `PedidoRepository` (como a `PedidoService`) pode receber um objeto de `InMemoryPedidoRepository` sem ter o seu comportamento ou execução quebrados.

---

### I - Interface Segregation Principle (Segregação de Interfaces)
Você não tem uma única interface gigante de repositório (ex: `Repository` contendo métodos para cliente, produto e pedido juntos). 
* **Onde está aplicado:**
  Você dividiu corretamente em:
  * `ClienteRepository` (exclusivo para Clientes)
  * `ProdutoRepository` (exclusivo para Produtos)
  * `PedidoRepository` (exclusivo para Pedidos)
  Dessa forma, quem precisa lidar apenas com produtos não é forçado a herdar ou implementar operações de clientes.

---

### D - Dependency Inversion Principle (Inversão de Dependência)
Você inverteu a dependência nas classes de serviço.
* **Onde está aplicado:**
  [PedidoService](file:///home/victor/Documentos/cafe/app/src/main/java/org/example/service/PedidoService.java) não instancia `InMemoryPedidoRepository` por conta própria. Ela recebe as interfaces via construtor:
  ```java
  public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
      this.pedidoRepository = pedidoRepository;
      this.clienteRepository = clienteRepository;
  }
  ```
  A classe de alto nível (`PedidoService`) depende de abstrações (`PedidoRepository`), e não de detalhes de baixo nível (`InMemoryPedidoRepository`).
