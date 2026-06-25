package org.example.service;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import java.util.List;

public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        return produtoRepository.salvar(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.buscarTodos();
    }

    public Produto buscarPorId(int id) {
        return produtoRepository.buscarPorId(id);
    }

    public void editar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        Produto existente = produtoRepository.buscarPorId(produto.getId());
        if (existente == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.salvar(produto);
    }

    public void excluir(int id) {
        Produto existente = produtoRepository.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.deletar(id);
    }
}
