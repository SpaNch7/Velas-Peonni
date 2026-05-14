package com.peonni.velas.controller;

import com.peonni.velas.entity.ItemPedido;
import com.peonni.velas.service.ItemPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService service;

    @PostMapping
    public ItemPedido criar(@RequestBody ItemPedido item) {
        return service.salvar(item);
    }

    @GetMapping
    public List<ItemPedido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ItemPedido buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
