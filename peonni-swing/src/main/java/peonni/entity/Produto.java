package peonni.entity;

import jakarta.persistence.*;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double preco;

    @Column(nullable = false)
    private int estoque;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Long getId()                   { return id; }
    public void setId(Long id)            { this.id = id; }
    public String getNome()               { return nome; }
    public void setNome(String nome)      { this.nome = nome; }
    public double getPreco()              { return preco; }
    public void setPreco(double preco)    { this.preco = preco; }
    public int getEstoque()               { return estoque; }
    public void setEstoque(int estoque)   { this.estoque = estoque; }
    public Categoria getCategoria()       { return categoria; }
    public void setCategoria(Categoria c) { this.categoria = c; }

    @Override
    public String toString() { return nome; }
}
