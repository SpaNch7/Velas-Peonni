package peonni.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }
    public LocalDate getData()                   { return data; }
    public void setData(LocalDate data)          { this.data = data; }
    public Cliente getCliente()                  { return cliente; }
    public void setCliente(Cliente cliente)      { this.cliente = cliente; }
    public List<ItemPedido> getItens()           { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
}
