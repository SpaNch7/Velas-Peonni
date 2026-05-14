package com.peonni.velas;
import com.peonni.velas.service.ClienteService;
import com.peonni.velas.service.PedidoService;
import com.peonni.velas.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class VelasApplication implements CommandLineRunner {
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private ClienteService clienteService;

	public static void main(String[] args) {
		SpringApplication.run(VelasApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Scanner sc = new Scanner(System.in);
		int opcao;

		do {
			System.out.println("\n=== PEONNI - SISTEMA DE VELAS ===");
			System.out.println("1 - Cadastrar Cliente");
			System.out.println("2 - Listar Produtos");
			System.out.println("3 - Criar Pedido");
			System.out.println("4 - Ver Pedidos");
			System.out.println("0 - Sair");
			System.out.print("Escolha: ");

			opcao = sc.nextInt();

			switch (opcao) {
				case 1:
					clienteService.cadastrarCliente();
					break;
				case 2:
					produtoService.listarProdutos();
					break;
				case 3:
					pedidoService.criarPedido();
					break;
				case 4:
					pedidoService.listarPedidos();
					break;
				case 0:
					System.out.println("Saindo...");
					break;
				default:
					System.out.println("Opção inválida!");
			}

		} while (opcao != 0);

		sc.close();
	}
}