package com.peonni.velas.service;

import com.peonni.velas.entity.Produto;
import com.peonni.velas.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public Produto salvar(Produto produto) {
        try {
            if (produto.getNome() == null || produto.getNome().isBlank()) {
                throw new RuntimeException("Nome do produto não pode ser vazio");
            }
            if (produto.getPreco() <= 0) {
                throw new RuntimeException("Preço deve ser maior que zero");
            }
            if (produto.getEstoque() < 0) {
                throw new RuntimeException("Estoque não pode ser negativo");
            }
            return repository.save(produto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public List<Produto> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage());
        }
    }

    public Produto buscarPorId(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar produto: " + e.getMessage());
        }
    }

    public void listarProdutos() {
        try {
            List<Produto> produtos = listar();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }

            System.out.println("\n=== LISTA DE PRODUTOS ===");
            for (Produto p : produtos) {
                System.out.println("ID: " + p.getId());
                System.out.println("Nome: " + p.getNome());
                System.out.println("Preço: R$ " + p.getPreco());
                System.out.println("Estoque: " + p.getEstoque());
                System.out.println("----------------------");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
    }
}
