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
                    IO.println("\nObrigado por usar o Sistema de Cafeteria! Até logo.");
                    break;
                default:
                    IO.println("Opção inválida. Tente novamente.");
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
        IO.println("\n ─────────────────────────────── ");
        IO.println("      SISTEMA DE CAFETERIA       ");
        IO.println(" ─────────────────────────────── ");
        IO.println("Selecione o seu perfil de acesso:");
        IO.println("1. Gerente");
        IO.println("2. Atendente");
        IO.println("3. Cliente");
        IO.println("4. Sair");
        IO.println(" ─────────────────────────────── ");
    }

    private static void menuGerente() {
        boolean noMenu = true;
        while (noMenu) {
            IO.println("\n ─── MENU GERENTE ─────────────── ");
            IO.println("1. Cadastrar Produto");
            IO.println("2. Listar Todos os Produtos");
            IO.println("3. Editar Produto");
            IO.println("4. Excluir Produto");
            IO.println("5. Visualizar Todos os Pedidos");
            IO.println("6. Voltar ao Menu Principal");
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
                        IO.println("Opção inválida.");
                }
            } catch (Exception e) {
                IO.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarProduto() {
        IO.println("\n[Cadastro de Produto]");
        String nome = lerString("Nome do produto: ");
        double preco = lerDouble("Preço (ex: 4.50): ");
        boolean disponivel = lerBoolean("Disponível para venda? (S/N): ");

        Produto produto = new Produto(nome, preco, disponivel);
        Produto cadastrado = produtoService.cadastrar(produto);
        IO.println("Produto cadastrado com sucesso! ID: " + cadastrado.getId());
    }

    private static void listarTodosProdutos() {
        IO.println("\n[Lista de Todos os Produtos]");
        List<Produto> produtos = produtoService.buscarTodos();
        if (produtos.isEmpty()) {
            IO.println("Nenhum produto cadastrado.");
            return;
        }
        for (Produto p : produtos) {
            System.out.printf("ID: %d | Nome: %-20s | Preço: R$ %5.2f | Disponível: %s\n",
                    p.getId(), p.getNome(), p.getPreco(), p.isDisponivel() ? "Sim" : "Não");
        }
    }

    private static void editarProduto() {
        IO.println("\n[Editar Produto]");
        listarTodosProdutos();
        int id = lerInt("Digite o ID do produto que deseja editar: ");
        Produto p = produtoService.buscarPorId(id);
        if (p == null) {
            IO.println("Produto não encontrado.");
            return;
        }
        IO.println("Dados atuais: " + p.getNome() + " | R$ " + p.getPreco() + " | Disponível: " + (p.isDisponivel() ? "Sim" : "Não"));
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
        IO.println("Produto editado com sucesso!");
    }

    private static void excluirProduto() {
        IO.println("\n[Excluir Produto]");
        listarTodosProdutos();
        int id = lerInt("Digite o ID do produto que deseja excluir: ");
        produtoService.excluir(id);
        IO.println("Produto excluído com sucesso!");
    }

    private static void visualizarTodosPedidos() {
        IO.println("\n[Todos os Pedidos Realizados]");
        List<Pedido> pedidos = pedidoService.buscarTodos();
        if (pedidos.isEmpty()) {
            IO.println("Nenhum pedido registrado.");
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
            IO.println("\n ─── MENU ATENDENTE ───────────── ");
            IO.println("1. Cadastrar Cliente");
            IO.println("2. Listar Clientes");
            IO.println("3. Registrar Pedido");
            IO.println("4. Atualizar Status do Pedido");
            IO.println("5. Visualizar Todos os Pedidos");
            IO.println("6. Voltar ao Menu Principal");
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
                        IO.println("Opção inválida.");
                }
            } catch (Exception e) {
                IO.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void cadastrarCliente() {
        IO.println("\n[Cadastro de Cliente]");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String email = lerString("Email: ");

        Cliente cliente = new Cliente(nome, telefone, email);
        Cliente cadastrado = clienteService.cadastrar(cliente);
        IO.println("Cliente cadastrado com sucesso! ID: " + cadastrado.getId());
    }

    private static void listarClientes() {
        IO.println("\n[Lista de Clientes]");
        List<Cliente> clientes = clienteService.buscarTodos();
        if (clientes.isEmpty()) {
            IO.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.printf("ID: %d | Nome: %-20s | Tel: %-15s | Email: %s\n",
                    c.getId(), c.getNome(), c.getTelefone(), c.getEmail());
        }
    }

    private static void registrarPedido() {
        IO.println("\n[Registrar Pedido]");
        listarClientes();
        int clienteId = lerInt("Digite o ID do Cliente: ");
        Cliente cliente = clienteService.buscarPorId(clienteId);

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        boolean adicionando = true;
        while (adicionando) {
            IO.println("\nProdutos Disponíveis:");
            List<Produto> disponiveis = new ArrayList<>();
            for (Produto p : produtoService.buscarTodos()) {
                if (p.isDisponivel()) {
                    disponiveis.add(p);
                    System.out.printf("  ID: %d | %-20s | R$ %.2f\n", p.getId(), p.getNome(), p.getPreco());
                }
            }
            if (disponiveis.isEmpty()) {
                IO.println("  Nenhum produto disponível no momento.");
                break;
            }

            int prodId = lerInt("Selecione o ID do Produto (ou 0 para finalizar itens): ");
            if (prodId == 0) {
                break;
            }

            Produto produto = produtoService.buscarPorId(prodId);
            if (produto == null || !produto.isDisponivel()) {
                IO.println("Produto inválido ou indisponível.");
                continue;
            }

            int qtd = lerInt("Quantidade: ");
            if (qtd <= 0) {
                IO.println("Quantidade deve ser maior que zero.");
                continue;
            }

            ItemPedido item = new ItemPedido(produto, qtd);
            pedido.adicionarItem(item);
            IO.println("Item adicionado: " + produto.getNome() + " x" + qtd + " (Subtotal: R$ " + String.format("%.2f", item.getSubtotal()) + ")");
            IO.println("Total atual do pedido: R$ " + String.format("%.2f", pedido.getValorTotal()));
        }

        if (pedido.getItens().isEmpty()) {
            IO.println("Nenhum item adicionado. Pedido cancelado.");
            return;
        }

        pedidoService.cadastrar(pedido);
        IO.println("\nPedido registrado com sucesso! ID do Pedido: " + pedido.getId());
        IO.println("Status Inicial: " + pedido.getStatus());
        IO.println("Valor Total: R$ " + String.format("%.2f", pedido.getValorTotal()));
    }

    private static void atualizarStatusPedido() {
        IO.println("\n[Atualizar Status do Pedido]");
        visualizarTodosPedidos();
        int pedidoId = lerInt("Digite o ID do Pedido: ");
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido == null) {
            IO.println("Pedido não encontrado.");
            return;
        }

        IO.println("Status atual: " + pedido.getStatus());
        IO.println("Selecione o novo status:");
        IO.println("1. AGUARDANDO");
        IO.println("2. EM_PREPARO");
        IO.println("3. PRONTO");
        IO.println("4. ENTREGUE");
        IO.println("5. CANCELADO");
        String op = lerString("Escolha uma opção: ");

        StatusPedido novoStatus;
        switch (op) {
            case "1": novoStatus = StatusPedido.AGUARDANDO; break;
            case "2": novoStatus = StatusPedido.EM_PREPARO; break;
            case "3": novoStatus = StatusPedido.PRONTO; break;
            case "4": novoStatus = StatusPedido.ENTREGUE; break;
            case "5": novoStatus = StatusPedido.CANCELADO; break;
            default:
                IO.println("Opção inválida. Status não alterado.");
                return;
        }

        pedidoService.atualizarStatus(pedidoId, novoStatus);
        IO.println("Status do pedido " + pedidoId + " atualizado para " + novoStatus + "!");
    }

    private static void menuCliente() {
        boolean noMenu = true;
        while (noMenu) {
            IO.println("\n ─── MENU CLIENTE ──────────────── ");
            IO.println("1. Visualizar Cardápio (Produtos Disponíveis)");
            IO.println("2. Visualizar Resumo de um Pedido");
            IO.println("3. Voltar ao Menu Principal");
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
                        IO.println("Opção inválida.");
                }
            } catch (Exception e) {
                IO.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void visualizarCardapio() {
        IO.println("\n ─── CARDÁPIO (PRODUTOS) ───────── ");
        List<Produto> produtos = produtoService.buscarTodos();
        boolean temDisponivel = false;
        for (Produto p : produtos) {
            if (p.isDisponivel()) {
                System.out.printf("  %-25s | Preço: R$ %5.2f\n", p.getNome(), p.getPreco());
                temDisponivel = true;
            }
        }
        if (!temDisponivel) {
            IO.println("Desculpe, nenhum produto disponível no momento.");
        }
        IO.println("🍰 ───────────────────────────────── 🍰");
    }

    private static void visualizarResumoPedido() {
        IO.println("\n[Resumo do Pedido]");
        int pedidoId = lerInt("Digite o ID do seu Pedido: ");
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido == null) {
            IO.println("Pedido não encontrado.");
            return;
        }

        IO.println("\n ─────────────────────────────── ");
        IO.println("       DETALHES DO PEDIDO #" + pedido.getId());
        IO.println(" ─────────────────────────────── ");
        IO.println("Cliente: " + pedido.getCliente().getNome());
        IO.println("Data/Hora: " + pedido.getDataCriacao().toString());
        IO.println("Status: " + pedido.getStatus());
        IO.println("  ───────────────────────────────  ");
        IO.println("Itens do Pedido:");
        for (ItemPedido item : pedido.getItens()) {
            System.out.printf("  %s (x%d) - R$ %.2f\n",
                    item.getProduto().getNome(), item.getQuantidade(), item.getSubtotal());
        }
        IO.println("  ───────────────────────────────  ");
        System.out.printf("VALOR TOTAL: R$ %.2f\n", pedido.getValorTotal());
        IO.println(" ─────────────────────────────── ");
    }

    private static String lerString(String prompt) {
        IO.print(prompt);
        return scanner.nextLine();
    }

    private static int lerInt(String valor) {
        while (true) {
            try {
                IO.print(valor);
                String linha = scanner.nextLine();
                return Integer.parseInt(linha.trim());
            } catch (NumberFormatException e) {
                IO.println("Número inteiro inválido. Tente novamente.");
            }
        }
    }

    private static double lerDouble(String valor) {
        while (true) {
            try {
                IO.print(valor);
                String linha = scanner.nextLine();
                return Double.parseDouble(linha.trim().replace(",", "."));
            } catch (NumberFormatException e) {
                IO.println("Valor numérico inválido. Tente novamente.");
            }
        }
    }

    private static boolean lerBoolean(String valor) {
        while (true) {
            IO.print(valor);
            String linha = scanner.nextLine().trim();
            if (linha.equalsIgnoreCase("S") || linha.equalsIgnoreCase("Sim")) {
                return true;
            } else if (linha.equalsIgnoreCase("N") || linha.equalsIgnoreCase("Não") || linha.equalsIgnoreCase("Nao")) {
                return false;
            } else {
                IO.println("Resposta inválida. Digite 'S' ou 'N'.");
            }
        }
    }
}
