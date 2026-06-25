package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    void deveCalcularSubtotalCorretamenteComDiferentesQuantidades() {
        Produto cafe = new Produto("Café", 4.50, true);

        ItemPedido item1 = new ItemPedido(cafe, 1);
        assertEquals(4.50, item1.calcularSubtotal(), 0.001);

        ItemPedido item2 = new ItemPedido(cafe, 3);
        assertEquals(13.50, item2.calcularSubtotal(), 0.001);

        ItemPedido itemZero = new ItemPedido(cafe, 0);
        assertEquals(0.00, itemZero.calcularSubtotal(), 0.001);
    }
}
