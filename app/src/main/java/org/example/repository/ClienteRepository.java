package org.example.repository;

import org.example.model.Cliente;
import java.util.List;

public interface ClienteRepository {
    Cliente salvar(Cliente cliente);
    Cliente buscarPorId(int id);
    List<Cliente> buscarTodos();
}
