package com.peonni.velas.service;

import com.peonni.velas.entity.Cliente;
import com.peonni.velas.entity.ItemPedido;
import com.peonni.velas.entity.Pedido;
import com.peonni.velas.entity.Produto;
import com.peonni.velas.repository.ClienteRepository;
import com.peonni.velas.repository.ItemPedidoRepository;
import com.peonni.velas.repository.PedidoRepository;
import com.peonni.velas.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Pedido salvar(Pedido pedido) {
        try {
            if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
                throw new RuntimeException("Pedido deve ter pelo menos um item");
            }
            pedido.getItens().forEach(item -> item.setPedido(pedido));
            return repository.save(pedido);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar pedido: " + e.getMessage());
        }
    }

    public Pedido buscarPorId(Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado com id: " + id));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pedido: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        try {
            repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar pedido: " + e.getMessage());
        }
    }

    public List<Pedido> listar() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    public void criarPedido() {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.print("ID do cliente: ");
            Long clienteId = Long.parseLong(sc.nextLine());

            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            System.out.print("ID do produto: ");
            Long produtoId = Long.parseLong(sc.nextLine());

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(sc.nextLine());

            if (quantidade <= 0) {
                System.out.println("Quantidade inválida!");
                return;
            }

            if (quantidade > produto.getEstoque()) {
                System.out.println("Estoque insuficiente!");
                return;
            }

            Pedido pedido = new Pedido();
            pedido.setCliente(cliente);
            pedido.setData(LocalDate.now());

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPedido(pedido);

            List<ItemPedido> itens = new ArrayList<>();
            itens.add(item);
            pedido.setItens(itens);

            repository.save(pedido);

            System.out.println("Pedido criado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void listarPedidos() {
        try {
            List<Pedido> pedidos = repository.findAll();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }

            System.out.println("\n=== LISTA DE PEDIDOS ===");

            for (Pedido p : pedidos) {
                System.out.println("\nPedido ID: " + p.getId());
                System.out.println("Cliente: " + p.getCliente().getNome());
                System.out.println("Data: " + p.getData());
                System.out.println("Itens:");
                for (ItemPedido item : p.getItens()) {
                    System.out.println("  - " + item.getProduto().getNome()
                            + " | Quantidade: " + item.getQuantidade());
                }
                System.out.println("------------------------");
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }
}
