package org.example.repository;

import org.example.model.Pedido;
import java.util.List;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    List<Pedido> buscarTodos();
    Pedido buscarPorId(int id);
}
