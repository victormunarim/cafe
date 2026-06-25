package org.example.service;

import org.example.model.*;
import org.example.repository.ClienteRepository;
import org.example.repository.PedidoRepository;
import java.util.List;

public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    public double calcularTotal(Pedido pedido) {
        if (pedido == null) {
            return 0.0;
        }
        return pedido.calcularTotal();
    }

    public void cadastrar(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }
        if (pedido.getCliente() == null) {
            throw new IllegalArgumentException("Pedido deve pertencer a um cliente");
        }
        if (pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter pelo menos um item");
        }
        Cliente cliente = clienteRepository.buscarPorId(pedido.getCliente().getId());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente do pedido não encontrado");
        }
        pedido.calcularTotal();
        pedidoRepository.salvar(pedido);
    }

    public void atualizarStatus(int id, StatusPedido status) {
        Pedido pedido = pedidoRepository.buscarPorId(id);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        pedido.setStatus(status);
        pedidoRepository.salvar(pedido);
    }

    public Pedido buscarPorId(int id) {
        return pedidoRepository.buscarPorId(id);
    }

    public List<Pedido> buscarTodos() {
        return pedidoRepository.buscarTodos();
    }
}
