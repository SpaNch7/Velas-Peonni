package peonni.ui;

import jakarta.persistence.EntityManager;
import peonni.entity.*;
import peonni.util.JPAUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelaPedido extends JFrame {

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaPedido() {
        setTitle("Cadastro de Pedidos");
        setSize(700, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblCliente    = new JLabel("Cliente:");
        JLabel lblProduto    = new JLabel("Produto:");
        JLabel lblQuantidade = new JLabel("Quantidade:");

        cbCliente     = new JComboBox<>();
        cbProduto     = new JComboBox<>();
        txtQuantidade = new JTextField(20);

        painelForm.add(lblCliente);    painelForm.add(cbCliente);
        painelForm.add(lblProduto);    painelForm.add(cbProduto);
        painelForm.add(lblQuantidade); painelForm.add(txtQuantidade);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        JButton btnSalvar  = new JButton("Fazer Pedido");
        JButton btnDeletar = new JButton("Cancelar Pedido");
        JButton btnLimpar  = new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);

        // Tabela agora inclui coluna de Total
        modelo = new DefaultTableModel(
                new String[]{"ID", "Cliente", "Produto", "Qtd", "Preco Unit.", "Total", "Data"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ajusta largura das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(3).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.NORTH);
        painelTopo.add(painelBotoes, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        carregarClientes();
        carregarProdutos();
        carregarTabela();

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtQuantidade.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Informe a quantidade!");
                    return;
                }
                try {
                    int qtd = Integer.parseInt(txtQuantidade.getText().trim());

                    if (qtd <= 0) {
                        JOptionPane.showMessageDialog(null, "Quantidade deve ser maior que zero!");
                        return;
                    }

                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Cliente cliente = em.find(Cliente.class,
                            ((Cliente) cbCliente.getSelectedItem()).getId());
                    Produto produto = em.find(Produto.class,
                            ((Produto) cbProduto.getSelectedItem()).getId());

                    if (produto.getEstoque() < qtd) {
                        JOptionPane.showMessageDialog(null,
                                "Estoque insuficiente! Disponivel: " + produto.getEstoque());
                        em.getTransaction().rollback();
                        em.close();
                        return;
                    }

                    double total = produto.getPreco() * qtd;

                    Pedido pedido = new Pedido();
                    pedido.setCliente(cliente);
                    pedido.setData(LocalDate.now());

                    ItemPedido item = new ItemPedido();
                    item.setProduto(produto);
                    item.setQuantidade(qtd);
                    item.setPedido(pedido);

                    List<ItemPedido> itens = new ArrayList<>();
                    itens.add(item);
                    pedido.setItens(itens);

                    produto.setEstoque(produto.getEstoque() - qtd);
                    em.merge(produto);

                    em.persist(pedido);
                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null,
                            "Pedido realizado com sucesso!\n" +
                            "Produto: " + produto.getNome() + "\n" +
                            "Quantidade: " + qtd + "\n" +
                            "Total: R$ " + String.format("%.2f", total));
                    limpar();
                    carregarProdutos(); // atualiza estoque no combobox
                    carregarTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Quantidade deve ser um numero inteiro!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        btnDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linha = tabela.getSelectedRow();
                if (linha < 0) {
                    JOptionPane.showMessageDialog(null, "Selecione um pedido na tabela!");
                    return;
                }
                int opcao = JOptionPane.showConfirmDialog(null,
                        "Deseja cancelar este pedido?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        Long idPedido = (Long) modelo.getValueAt(linha, 0);

                        EntityManager em = JPAUtil.getEntityManager();
                        em.getTransaction().begin();

                        Pedido pedido = em.find(Pedido.class, idPedido);

                        // Devolve estoque ao cancelar
                        for (ItemPedido item : pedido.getItens()) {
                            Produto prod = em.find(Produto.class, item.getProduto().getId());
                            prod.setEstoque(prod.getEstoque() + item.getQuantidade());
                            em.merge(prod);
                        }

                        em.remove(pedido);
                        em.getTransaction().commit();
                        em.close();

                        JOptionPane.showMessageDialog(null, "Pedido cancelado e estoque devolvido!");
                        carregarProdutos();
                        carregarTabela();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                    }
                }
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });
    }

    private void carregarClientes() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Cliente> lista = em.createQuery("SELECT c FROM Cliente c ORDER BY c.nome", Cliente.class).getResultList();
            em.close();
            cbCliente.removeAllItems();
            for (Cliente c : lista) cbCliente.addItem(c);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void carregarProdutos() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Produto> lista = em.createQuery("SELECT p FROM Produto p ORDER BY p.nome", Produto.class).getResultList();
            em.close();
            cbProduto.removeAllItems();
            for (Produto p : lista) cbProduto.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Pedido> lista = em.createQuery(
                    "SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente JOIN FETCH p.itens i JOIN FETCH i.produto ORDER BY p.id DESC",
                    Pedido.class).getResultList();
            em.close();
            modelo.setRowCount(0);
            for (Pedido p : lista) {
                for (ItemPedido item : p.getItens()) {
                    double precoUnit = item.getProduto().getPreco();
                    int qtd = item.getQuantidade();
                    double total = precoUnit * qtd;
                    modelo.addRow(new Object[]{
                        p.getId(),
                        p.getCliente().getNome(),
                        item.getProduto().getNome(),
                        qtd,
                        String.format("R$ %.2f", precoUnit),
                        String.format("R$ %.2f", total),
                        p.getData()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    private void limpar() {
        txtQuantidade.setText("");
        tabela.clearSelection();
    }
}
