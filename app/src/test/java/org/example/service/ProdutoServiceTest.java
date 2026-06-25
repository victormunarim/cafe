package org.example.service;

import org.example.model.Produto;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveCadastrarProdutoComSucesso() {
        Produto produto = new Produto("Café Espresso", 4.50, true);
        when(produtoRepository.salvar(produto)).thenReturn(produto);
        Produto resultado = produtoService.cadastrar(produto);
        assertNotNull(resultado);
        assertEquals("Café Espresso", resultado.getNome());
        verify(produtoRepository, times(1)).salvar(produto);
    }

    @Test
    void naoDeveCadastrarProdutoComPrecoNegativo() {
        Produto produto = new Produto("Café", -1.0, true);
        assertThrows(IllegalArgumentException.class,
                () -> produtoService.cadastrar(produto));
    }
}
