package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepository;
import java.util.List;

public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio");
        }
        return clienteRepository.salvar(cliente);
    }

    public Cliente buscarPorId(int id) {
        Cliente cliente = clienteRepository.buscarPorId(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        return cliente;
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.buscarTodos();
    }
}
