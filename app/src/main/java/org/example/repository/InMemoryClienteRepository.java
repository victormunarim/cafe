package org.example.repository;

import org.example.model.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryClienteRepository implements ClienteRepository {
    private final Map<Integer, Cliente> clientes = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Cliente salvar(Cliente cliente) {
        if (cliente.getId() == 0) {
            cliente.setId(nextId.getAndIncrement());
        }
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Cliente buscarPorId(int id) {
        return clientes.get(id);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return new ArrayList<>(clientes.values());
    }
}
