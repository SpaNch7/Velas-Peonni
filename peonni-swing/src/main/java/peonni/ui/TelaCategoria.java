package peonni.ui;

import jakarta.persistence.EntityManager;
import peonni.entity.Categoria;
import peonni.util.JPAUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaCategoria extends JFrame {

    private JTextField txtNome;
    private JTable tabela;
    private DefaultTableModel modelo;
    private Long idSelecionado = null;

    public TelaCategoria() {
        setTitle("Cadastro de Categorias");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel do formulário
        JPanel painelForm = new JPanel(new GridLayout(1, 2, 5, 5));
        painelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField(20);

        painelForm.add(lblNome);
        painelForm.add(txtNome);

        // Painel dos botões
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

        // Tabela
        modelo = new DefaultTableModel(new String[]{"ID", "Nome"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Painel superior juntando form + botões
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.add(painelForm, BorderLayout.NORTH);
        painelTopo.add(painelBotoes, BorderLayout.CENTER);

        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        carregarTabela();

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha o nome!");
                    return;
                }
                try {
                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Categoria c = new Categoria();
                    c.setNome(txtNome.getText().trim());
                    em.persist(c);

                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Categoria salva com sucesso!");
                    limpar();
                    carregarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione uma categoria na tabela!");
                    return;
                }
                if (txtNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha o nome!");
                    return;
                }
                try {
                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Categoria c = em.find(Categoria.class, idSelecionado);
                    c.setNome(txtNome.getText().trim());
                    em.merge(c);

                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Categoria atualizada!");
                    limpar();
                    carregarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        });

        btnDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione uma categoria na tabela!");
                    return;
                }
                int opcao = JOptionPane.showConfirmDialog(null,
                        "Deseja deletar esta categoria?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        EntityManager em = JPAUtil.getEntityManager();
                        em.getTransaction().begin();

                        Categoria c = em.find(Categoria.class, idSelecionado);
                        em.remove(c);

                        em.getTransaction().commit();
                        em.close();

                        JOptionPane.showMessageDialog(null, "Categoria deletada!");
                        limpar();
                        carregarTabela();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar. Verifique se há produtos nessa categoria!");
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
            }
        });
    }

    private void carregarTabela() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Categoria> lista = em.createQuery("SELECT c FROM Categoria c ORDER BY c.nome", Categoria.class).getResultList();
            em.close();

            modelo.setRowCount(0);
            for (Categoria c : lista) {
                modelo.addRow(new Object[]{c.getId(), c.getNome()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar: " + e.getMessage());
        }
    }

    private void limpar() {
        txtNome.setText("");
        idSelecionado = null;
        tabela.clearSelection();
    }
}
