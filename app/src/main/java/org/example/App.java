package org.example;

import org.example.model.*;
import org.example.repository.*;
import org.example.service.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static ProdutoService produtoService;
    private static ClienteService clienteService;
    private static PedidoService pedidoService;

    static void main(String[] args) {
        ProdutoRepository produtoRepository = new InMemoryProdutoRepository();
        ClienteRepository clienteRepository = new InMemoryClienteRepository();
        PedidoRepository pedidoRepository = new InMemoryPedidoRepository();

        produtoService = new ProdutoService(produtoRepository);
        clienteService = new ClienteService(clienteRepository);
        pedidoService = new PedidoService(pedidoRepository, clienteRepository);

        semearDados();

        boolean rodando = true;
        while (rodando) {
            exibirMenuPrincipal();
            String opcao = lerString("Escolha uma opção: ");
            switch (opcao) {
                case "1":
                    menuGerente();
                    break;
                case "2":
                    menuAtendente();
                    break;
                case "3":
                    menuCliente();
                    break;
                case "4":
                    rodando = false;
                    System.out.println("\nObrigado por usar o Sistema de Cafeteria! Até logo.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void semearDados() {
        produtoService.cadastrar(new Produto("Café Espresso", 4.50, true));
        produtoService.cadastrar(new Produto("Bolo de Chocolate", 8.00, true));
        produtoService.cadastrar(new Produto("Pão de Queijo", 5.50, true));
        produtoService.cadastrar(new Produto("Cappuccino", 7.00, true));
        produtoService.cadastrar(new Produto("Suco de Laranja", 6.00, false));

        clienteService.cadastrar(new Cliente("João Silva", "48 99999-1111", "joao@email.com"));
        clienteService.cadastrar(new Cliente("Maria Santos", "48 99999-2222", "maria@email.com"));
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n ─────────────────────────────── ");
        System.out.println("      SISTEMA DE CAFETERIA       ");
        System.out.println(" ─────────────────────────────── ");
        System.out.println("Selecione o seu perfil de acesso:");
        System.out.println("1. Gerente");
        System.out.println("2. Atendente");
        System.out.println("3. Cliente");
        System.out.println("4. Sair");
        System.out.println(" ─────────────────────────────── ");
    }

    private static void menuGerente() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n ─── MENU GERENTE ─────────────── ");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Todos os Produtos");
            System.out.println("3. Editar Produto");
            System.out.println("4. Excluir Produto");
            System.out.println("5. Visualizar Todos os Pedidos");
            System.out.println("6. Voltar ao Menu Principal");
            String opcao = lerString("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case "1":
                        cadastrarProduto();
                        break;
                    case "2":
                        listarTodosProdutos();
                        break;
                    case "3":
                        editarProduto();
                        break;
                    case "4":
                        excluirProduto();
                        break;
                    case "5":
                        visualizarTodosPedidos();
                        break;
                    case "6":
                        noMenu = false;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarProduto() {
        System.out.println("\n[Cadastro de Produto]");
        String nome = lerString("Nome do produto: ");
        double preco = lerDouble("Preço (ex: 4.50): ");
        boolean disponivel = lerBoolean("Disponível para venda? (S/N): ");

        Produto produto = new Produto(nome, preco, disponivel);
        Produto cadastrado = produtoService.cadastrar(produto);
        System.out.println("Produto cadastrado com sucesso! ID: " + cadastrado.getId());
    }

    private static void listarTodosProdutos() {
        System.out.println("\n[Lista de Todos os Produtos]");
        List<Produto> produtos = produtoService.buscarTodos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (Produto p : produtos) {
            System.out.printf("ID: %d | Nome: %-20s | Preço: R$ %5.2f | Disponível: %s\n",
                    p.getId(), p.getNome(), p.getPreco(), p.isDisponivel() ? "Sim" : "Não");
        }
    }

    private static void editarProduto() {
        System.out.println("\n[Editar Produto]");
        listarTodosProdutos();
        int id = lerInt("Digite o ID do produto que deseja editar: ");
        Produto p = produtoService.buscarPorId(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + p.getNome() + " | R$ " + p.getPreco() + " | Disponível: " + (p.isDisponivel() ? "Sim" : "Não"));
        String novoNome = lerString("Novo nome (deixe vazio para manter): ");
        if (!novoNome.trim().isEmpty()) {
            p.setNome(novoNome);
        }
        String precoStr = lerString("Novo preço (deixe vazio para manter): ");
        if (!precoStr.trim().isEmpty()) {
            p.setPreco(Double.parseDouble(precoStr));
        }
        String dispStr = lerString("Disponível? (S/N) (deixe vazio para manter): ");
        if (!dispStr.trim().isEmpty()) {
            p.setDisponivel(dispStr.equalsIgnoreCase("S"));
        }
        produtoService.editar(p);
        System.out.println("Produto editado com sucesso!");
    }

    private static void excluirProduto() {
        System.out.println("\n[Excluir Produto]");
        listarTodosProdutos();
        int id = lerInt("Digite o ID do produto que deseja excluir: ");
        produtoService.excluir(id);
        System.out.println("Produto excluído com sucesso!");
    }

    private static void visualizarTodosPedidos() {
        System.out.println("\n[Todos os Pedidos Realizados]");
        List<Pedido> pedidos = pedidoService.buscarTodos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.printf("Pedido ID: %d | Cliente: %s | Data: %s | Status: %s | Total: R$ %.2f\n",
                    p.getId(), p.getCliente().getNome(), p.getDataCriacao().toString(), p.getStatus(), p.getValorTotal());
        }
    }

    private static void menuAtendente() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n ─── MENU ATENDENTE ───────────── ");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Registrar Pedido");
            System.out.println("4. Atualizar Status do Pedido");
            System.out.println("5. Visualizar Todos os Pedidos");
            System.out.println("6. Voltar ao Menu Principal");
            String opcao = lerString("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case "1":
                        cadastrarCliente();
                        break;
                    case "2":
                        listarClientes();
                        break;
                    case "3":
                        registrarPedido();
                        break;
                    case "4":
                        atualizarStatusPedido();
                        break;
                    case "5":
                        visualizarTodosPedidos();
                        break;
                    case "6":
                        noMenu = false;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarCliente() {
        System.out.println("\n[Cadastro de Cliente]");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String email = lerString("Email: ");

        Cliente cliente = new Cliente(nome, telefone, email);
        Cliente cadastrado = clienteService.cadastrar(cliente);
        System.out.println("Cliente cadastrado com sucesso! ID: " + cadastrado.getId());
    }

    private static void listarClientes() {
        System.out.println("\n[Lista de Clientes]");
        List<Cliente> clientes = clienteService.buscarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.printf("ID: %d | Nome: %-20s | Tel: %-15s | Email: %s\n",
                    c.getId(), c.getNome(), c.getTelefone(), c.getEmail());
        }
    }

    private static void registrarPedido() {
        System.out.println("\n[Registrar Pedido]");
        listarClientes();
        int clienteId = lerInt("Digite o ID do Cliente: ");
        Cliente cliente = clienteService.buscarPorId(clienteId);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        boolean adicionando = true;
        while (adicionando) {
            System.out.println("\nProdutos Disponíveis:");
            List<Produto> disponiveis = new ArrayList<>();
            for (Produto p : produtoService.buscarTodos()) {
                if (p.isDisponivel()) {
                    disponiveis.add(p);
                    System.out.printf("  ID: %d | %-20s | R$ %.2f\n", p.getId(), p.getNome(), p.getPreco());
                }
            }
            if (disponiveis.isEmpty()) {
                System.out.println("  Nenhum produto disponível no momento.");
                break;
            }

            int prodId = lerInt("Selecione o ID do Produto (ou 0 para finalizar itens): ");
            if (prodId == 0) {
                break;
            }

            Produto produto = produtoService.buscarPorId(prodId);
            if (produto == null || !produto.isDisponivel()) {
                System.out.println("Produto inválido ou indisponível.");
                continue;
            }

            int qtd = lerInt("Quantidade: ");
            if (qtd <= 0) {
                System.out.println("Quantidade deve ser maior que zero.");
                continue;
            }

            ItemPedido item = new ItemPedido(produto, qtd);
            pedido.adicionarItem(item);
            System.out.println("Item adicionado: " + produto.getNome() + " x" + qtd + " (Subtotal: R$ " + String.format("%.2f", item.getSubtotal()) + ")");
            System.out.println("Total atual do pedido: R$ " + String.format("%.2f", pedido.getValorTotal()));
        }

        if (pedido.getItens().isEmpty()) {
            System.out.println("Nenhum item adicionado. Pedido cancelado.");
            return;
        }

        pedidoService.cadastrar(pedido);
        System.out.println("\nPedido registrado com sucesso! ID do Pedido: " + pedido.getId());
        System.out.println("Status Inicial: " + pedido.getStatus());
        System.out.println("Valor Total: R$ " + String.format("%.2f", pedido.getValorTotal()));
    }

    private static void atualizarStatusPedido() {
        System.out.println("\n[Atualizar Status do Pedido]");
        visualizarTodosPedidos();
        int pedidoId = lerInt("Digite o ID do Pedido: ");
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        System.out.println("Status atual: " + pedido.getStatus());
        System.out.println("Selecione o novo status:");
        System.out.println("1. AGUARDANDO");
        System.out.println("2. EM_PREPARO");
        System.out.println("3. PRONTO");
        System.out.println("4. ENTREGUE");
        System.out.println("5. CANCELADO");
        String op = lerString("Escolha uma opção: ");

        StatusPedido novoStatus;
        switch (op) {
            case "1": novoStatus = StatusPedido.AGUARDANDO; break;
            case "2": novoStatus = StatusPedido.EM_PREPARO; break;
            case "3": novoStatus = StatusPedido.PRONTO; break;
            case "4": novoStatus = StatusPedido.ENTREGUE; break;
            case "5": novoStatus = StatusPedido.CANCELADO; break;
            default:
                System.out.println("Opção inválida. Status não alterado.");
                return;
        }

        pedidoService.atualizarStatus(pedidoId, novoStatus);
        System.out.println("Status do pedido " + pedidoId + " atualizado para " + novoStatus + "!");
    }

    private static void menuCliente() {
        boolean noMenu = true;
        while (noMenu) {
            System.out.println("\n ─── MENU CLIENTE ──────────────── ");
            System.out.println("1. Visualizar Cardápio (Produtos Disponíveis)");
            System.out.println("2. Visualizar Resumo de um Pedido");
            System.out.println("3. Voltar ao Menu Principal");
            String opcao = lerString("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case "1":
                        visualizarCardapio();
                        break;
                    case "2":
                        visualizarResumoPedido();
                        break;
                    case "3":
                        noMenu = false;
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void visualizarCardapio() {
        System.out.println("\n ─── CARDÁPIO (PRODUTOS) ───────── ");
        List<Produto> produtos = produtoService.buscarTodos();
        boolean temDisponivel = false;
        for (Produto p : produtos) {
            if (p.isDisponivel()) {
                System.out.printf("  %-25s | Preço: R$ %5.2f\n", p.getNome(), p.getPreco());
                temDisponivel = true;
            }
        }
        if (!temDisponivel) {
            System.out.println("Desculpe, nenhum produto disponível no momento.");
        }
        System.out.println("🍰 ───────────────────────────────── 🍰");
    }

    private static void visualizarResumoPedido() {
        System.out.println("\n[Resumo do Pedido]");
        int pedidoId = lerInt("Digite o ID do seu Pedido: ");
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        System.out.println("\n ─────────────────────────────── ");
        System.out.println("       DETALHES DO PEDIDO #" + pedido.getId());
        System.out.println(" ─────────────────────────────── ");
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Data/Hora: " + pedido.getDataCriacao().toString());
        System.out.println("Status: " + pedido.getStatus());
        System.out.println("  ───────────────────────────────  ");
        System.out.println("Itens do Pedido:");
        for (ItemPedido item : pedido.getItens()) {
            System.out.printf("  %s (x%d) - R$ %.2f\n",
                    item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal());
        }
        System.out.println("  ───────────────────────────────  ");
        System.out.printf("VALOR TOTAL: R$ %.2f\n", pedido.getValorTotal());
        System.out.println(" ─────────────────────────────── ");
    }

    private static String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int lerInt(String valor) {
        while (true) {
            try {
                System.out.print(valor);
                String linha = scanner.nextLine();
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                System.out.println("Número inteiro inválido. Tente novamente.");
            }
        }
    }

    private static double lerDouble(String valor) {
        while (true) {
            try {
                System.out.print(valor);
                String linha = scanner.nextLine();
                return Double.parseDouble(linha.trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor numérico inválido. Tente novamente.");
            }
        }
    }

    private static boolean lerBoolean(String valor) {
        while (true) {
            System.out.print(valor);
            String linha = scanner.nextLine().trim();
            if (linha.equalsIgnoreCase("S") || linha.equalsIgnoreCase("Sim")) {
                return true;
            } else if (linha.equalsIgnoreCase("N") || linha.equalsIgnoreCase("Não") || linha.equalsIgnoreCase("Nao")) {
                return false;
            } else {
                System.out.println("Resposta inválida. Digite 'S' ou 'N'.");
            }
        }
    }
}
