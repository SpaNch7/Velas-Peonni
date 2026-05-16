package peonni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Long getId()                { return id; }
    public void setId(Long id)         { this.id = id; }
    public int getQuantidade()         { return quantidade; }
    public void setQuantidade(int q)   { this.quantidade = q; }
    public Pedido getPedido()          { return pedido; }
    public void setPedido(Pedido p)    { this.pedido = p; }
    public Produto getProduto()        { return produto; }
    public void setProduto(Produto p)  { this.produto = p; }
}
