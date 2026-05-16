package peonni.entity;

import jakarta.persistence.*;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Long getId()              { return id; }
    public void setId(Long id)       { this.id = id; }
    public String getNome()          { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() { return nome; }
}
