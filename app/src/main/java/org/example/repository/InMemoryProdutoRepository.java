package org.example.repository;

import org.example.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryProdutoRepository implements ProdutoRepository {
    private final Map<Integer, Produto> produtos = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Produto salvar(Produto produto) {
        if (produto.getId() == 0) {
            produto.setId(nextId.getAndIncrement());
        }
        produtos.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public List<Produto> buscarTodos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public Produto buscarPorId(int id) {
        return produtos.get(id);
    }

    @Override
    public void deletar(int id) {
        produtos.remove(id);
    }
}
