package org.example.repository;

import org.example.model.Produto;
import java.util.List;

public interface ProdutoRepository {
    Produto salvar(Produto produto);
    List<Produto> buscarTodos();
    Produto buscarPorId(int id);
    void deletar(int id);
}
