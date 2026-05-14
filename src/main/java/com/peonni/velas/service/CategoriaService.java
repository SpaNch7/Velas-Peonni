package com.peonni.velas.service;

import com.peonni.velas.entity.Categoria;
import com.peonni.velas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public Categoria salvar(Categoria categoria) {
        try {
            if (categoria.getNome() == null || categoria.getNome().isBlank()) {
                throw new RuntimeException("Nome da categoria não pode ser vazio");
            }
            return repository.save(categoria);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    public List<Categoria> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar categorias: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar categoria: " + e.getMessage());
        }
    }

    public Categoria buscarPorId(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar categoria: " + e.getMessage());
        }
    }
}
