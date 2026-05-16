package peonni.ui;

import jakarta.persistence.EntityManager;
import peonni.entity.Categoria;
import peonni.entity.Produto;
import peonni.util.JPAUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaProduto extends JFrame {

    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JComboBox<Categoria> cbCategoria;
    private JTable tabela;
    private DefaultTableModel modelo;
    private Long idSelecionado = null;

    public TelaProduto() {
        setTitle("Cadastro de Produtos");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel painelForm = new JPanel(new GridLayout(4, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblNome      = new JLabel("Nome:");
        JLabel lblPreco     = new JLabel("Preco:");
        JLabel lblEstoque   = new JLabel("Estoque:");
        JLabel lblCategoria = new JLabel("Categoria:");

        txtNome     = new JTextField(20);
        txtPreco    = new JTextField(20);
        txtEstoque  = new JTextField(20);
        cbCategoria = new JComboBox<>();

        painelForm.add(lblNome);      painelForm.add(txtNome);
        painelForm.add(lblPreco);     painelForm.add(txtPreco);
        painelForm.add(lblEstoque);   painelForm.add(txtEstoque);
        painelForm.add(lblCategoria); painelForm.add(cbCategoria);

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        JButton btnSalvar  = new JButton("Salvar");
        JButton btnEditar  = new JButton("Editar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnLimpar  = new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Nome", "Preco", "Estoque", "Categoria"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.NORTH);
        painelTopo.add(painelBotoes, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        carregarCategorias();
        carregarTabela();

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().trim().isEmpty() || txtPreco.getText().trim().isEmpty()
                        || txtEstoque.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                    return;
                }
                try {
                    double preco = Double.parseDouble(txtPreco.getText().replace(",", "."));
                    int estoque  = Integer.parseInt(txtEstoque.getText().trim());

                    if (preco <= 0) {
                        JOptionPane.showMessageDialog(null, "Preco deve ser maior que zero!");
                        return;
                    }
                    if (estoque < 0) {
                        JOptionPane.showMessageDialog(null, "Estoque nao pode ser negativo!");
                        return;
                    }

                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Produto p = new Produto();
                    p.setNome(txtNome.getText().trim());
                    p.setPreco(preco);
                    p.setEstoque(estoque);

                    Categoria cat = (Categoria) cbCategoria.getSelectedItem();
                    if (cat != null) {
                        p.setCategoria(em.find(Categoria.class, cat.getId()));
                    }

                    em.persist(p);
                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Produto salvo com sucesso!");
                    limpar();
                    carregarTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Preco e Estoque devem ser numeros!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um produto na tabela!");
                    return;
                }
                try {
                    double preco   = Double.parseDouble(txtPreco.getText().replace(",", "."));
                    int estoque    = Integer.parseInt(txtEstoque.getText().trim());

                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Produto p = em.find(Produto.class, idSelecionado);
                    p.setNome(txtNome.getText().trim());
                    p.setPreco(preco);
                    p.setEstoque(estoque);

                    Categoria cat = (Categoria) cbCategoria.getSelectedItem();
                    if (cat != null) {
                        p.setCategoria(em.find(Categoria.class, cat.getId()));
                    }

                    em.merge(p);
                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Produto atualizado!");
                    limpar();
                    carregarTabela();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Preco e Estoque devem ser numeros!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        btnDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um produto na tabela!");
                    return;
                }
                int opcao = JOptionPane.showConfirmDialog(null,
                        "Deseja deletar este produto?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        EntityManager em = JPAUtil.getEntityManager();
                        em.getTransaction().begin();

                        Produto p = em.find(Produto.class, idSelecionado);
                        em.remove(p);

                        em.getTransaction().commit();
                        em.close();

                        JOptionPane.showMessageDialog(null, "Produto deletado!");
                        limpar();
                        carregarTabela();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar. Verifique se ha pedidos com este produto!");
                    }
                }
            }
        });

        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });

        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                idSelecionado = (Long) modelo.getValueAt(linha, 0);
                txtNome.setText((String) modelo.getValueAt(linha, 1));
                txtPreco.setText(String.valueOf(modelo.getValueAt(linha, 2)));
                txtEstoque.setText(String.valueOf(modelo.getValueAt(linha, 3)));

                // Seleciona a categoria correta no combobox
                String nomeCategoria = (String) modelo.getValueAt(linha, 4);
                for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                    Categoria cat = cbCategoria.getItemAt(i);
                    if (cat.getNome().equals(nomeCategoria)) {
                        cbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
    }

    private void carregarCategorias() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Categoria> lista = em.createQuery("SELECT c FROM Categoria c ORDER BY c.nome", Categoria.class).getResultList();
            em.close();
            cbCategoria.removeAllItems();
            for (Categoria c : lista) {
                cbCategoria.addItem(c);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Produto> lista = em.createQuery(
                "SELECT p FROM Produto p LEFT JOIN FETCH p.categoria ORDER BY p.nome",
                Produto.class).getResultList();
            em.close();
            modelo.setRowCount(0);
            for (Produto p : lista) {
                String cat = p.getCategoria() != null ? p.getCategoria().getNome() : "-";
                modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getEstoque(), cat});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar: " + e.getMessage());
        }
    }

    private void limpar() {
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        idSelecionado = null;
        tabela.clearSelection();
    }
}
