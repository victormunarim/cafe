package org.example.model;

public class ItemPedido {
    private int id;
    private Produto produto;
    private int quantidade;
    private double subtotal;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.subtotal = calcularSubtotal();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public double calcularSubtotal() {
        if (produto == null) {
            return 0.0;
        }
        return produto.getPreco() * quantidade;
    }
}
