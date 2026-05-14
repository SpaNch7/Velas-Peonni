package com.peonni.velas.service;

import com.peonni.velas.entity.ItemPedido;
import com.peonni.velas.repository.ItemPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository repository;

    public ItemPedido salvar(ItemPedido item) {
        try {
            if (item.getQuantidade() <= 0) {
                throw new RuntimeException("Quantidade deve ser maior que zero");
            }
            return repository.save(item);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar item do pedido: " + e.getMessage());
        }
    }

    public List<ItemPedido> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar itens: " + e.getMessage());
        }
    }

    public ItemPedido buscarPorId(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado com id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar item: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar item: " + e.getMessage());
        }
    }
}
