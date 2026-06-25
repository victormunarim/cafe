package org.example.repository;

import org.example.model.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryPedidoRepository implements PedidoRepository {
    private final Map<Integer, Pedido> pedidos = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Pedido salvar(Pedido pedido) {
        if (pedido.getId() == 0) {
            pedido.setId(nextId.getAndIncrement());
        }
        pedidos.put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public List<Pedido> buscarTodos() {
        return new ArrayList<>(pedidos.values());
    }

    @Override
    public Pedido buscarPorId(int id) {
        return pedidos.get(id);
    }
}
