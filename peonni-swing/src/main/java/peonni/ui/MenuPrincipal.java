package peonni.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import peonni.config.FlyWayConfig;
import peonni.util.JPAUtil;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Peonni - Velas Aromaticas");
        setSize(300, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 5, 5));

        JLabel lblTitulo = new JLabel("Peonni - Velas Aromaticas", SwingConstants.CENTER);

        JButton btnClientes   = new JButton("Clientes");
        JButton btnProdutos   = new JButton("Produtos");
        JButton btnCategorias = new JButton("Categorias");
        JButton btnPedidos    = new JButton("Pedidos");

        panel.add(btnClientes);
        panel.add(btnProdutos);
        panel.add(btnCategorias);
        panel.add(btnPedidos);

        add(lblTitulo, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        // ActionListeners - estilo da pratica 05 do professor
        btnClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TelaCliente().setVisible(true);
            }
        });

        btnProdutos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TelaProduto().setVisible(true);
            }
        });

        btnCategorias.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TelaCategoria().setVisible(true);
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new TelaPedido().setVisible(true);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        // Roda as migrations antes de abrir as telas
        FlyWayConfig.migrate();
        new MenuPrincipal();
    }
}
