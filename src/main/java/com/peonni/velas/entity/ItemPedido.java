package com.peonni.velas.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;

    @ManyToOne
    private Pedido pedido;

    @ManyToOne
    private Produto produto;
}