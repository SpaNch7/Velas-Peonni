package com.peonni.velas.service;

import com.peonni.velas.entity.Cliente;
import com.peonni.velas.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente salvar(Cliente cliente) {
        try {
            boolean emailDuplicado = repository.findAll().stream()
                    .anyMatch(c -> c.getEmail().equals(cliente.getEmail())
                            && !c.getId().equals(cliente.getId()));
            if (emailDuplicado) {
                throw new RuntimeException("Email já cadastrado");
            }
            return repository.save(cliente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    public void cadastrarCliente() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEmail(email);

            salvar(cliente);

            System.out.println("Cliente salvo no banco!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarClientes() {
        try {
            List<Cliente> clientes = listar();

            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
                return;
            }

            System.out.println("\n=== LISTA DE CLIENTES ===");
            for (Cliente c : clientes) {
                System.out.println("ID: " + c.getId());
                System.out.println("Nome: " + c.getNome());
                System.out.println("Email: " + c.getEmail());
                System.out.println("----------------------");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
    }
}
