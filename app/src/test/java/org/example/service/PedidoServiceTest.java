package org.example.service;

import org.example.model.*;
import org.example.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void deveCalcularTotalCorretamente() {
        Produto cafe = new Produto("Café", 4.50, true);
        Produto bolo = new Produto("Bolo", 8.00, true);
        Pedido pedido = new Pedido();
        pedido.adicionarItem(new ItemPedido(cafe, 2));
        pedido.adicionarItem(new ItemPedido(bolo, 1));
        double total = pedidoService.calcularTotal(pedido);
        assertEquals(17.00, total, 0.001);
    }

    @Test
    void deveAtualizarStatusDoPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(1);
        when(pedidoRepository.buscarPorId(1)).thenReturn(pedido);
        pedidoService.atualizarStatus(1, StatusPedido.EM_PREPARO);
        verify(pedidoRepository).salvar(pedido);
        assertEquals(StatusPedido.EM_PREPARO, pedido.getStatus());
    }
}
