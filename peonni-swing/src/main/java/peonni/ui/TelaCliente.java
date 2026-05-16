package peonni.ui;

import jakarta.persistence.EntityManager;
import peonni.entity.Cliente;
import peonni.util.JPAUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaCliente extends JFrame {

    private JTextField txtNome;
    private JTextField txtEmail;
    private JTable tabela;
    private DefaultTableModel modelo;
    private Long idSelecionado = null;

    public TelaCliente() {
        setTitle("Cadastro de Clientes");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel do formulario - GridLayout igual pratica do professor
        JPanel painelForm = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel lblNome  = new JLabel("Nome:");
        JLabel lblEmail = new JLabel("Email:");
        txtNome  = new JTextField(20);
        txtEmail = new JTextField(20);

        painelForm.add(lblNome);  painelForm.add(txtNome);
        painelForm.add(lblEmail); painelForm.add(txtEmail);
        painelForm.add(new JLabel()); // espaco vazio para manter layout

        // Painel dos botoes - FlowLayout igual pratica do professor
        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnSalvar  = new JButton("Salvar");
        JButton btnEditar  = new JButton("Editar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnLimpar  = new JButton("Limpar");

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnLimpar);

        // Tabela para listar clientes
        modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Email"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);

        // Adiciona tudo no frame - BorderLayout
        add(painelForm,  BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scroll,      BorderLayout.SOUTH);

        carregarTabela();

        // ActionListener botao Salvar - estilo pratica 05
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                    return;
                }
                try {
                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Cliente cliente = new Cliente();
                    cliente.setNome(txtNome.getText());
                    cliente.setEmail(txtEmail.getText());
                    em.persist(cliente);

                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Cliente salvo com sucesso!");
                    limpar();
                    carregarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage());
                }
            }
        });

        // ActionListener botao Editar
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela!");
                    return;
                }
                try {
                    EntityManager em = JPAUtil.getEntityManager();
                    em.getTransaction().begin();

                    Cliente cliente = em.find(Cliente.class, idSelecionado);
                    cliente.setNome(txtNome.getText());
                    cliente.setEmail(txtEmail.getText());
                    em.merge(cliente);

                    em.getTransaction().commit();
                    em.close();

                    JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");
                    limpar();
                    carregarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao editar: " + ex.getMessage());
                }
            }
        });

        // ActionListener botao Deletar
        btnDeletar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (idSelecionado == null) {
                    JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela!");
                    return;
                }
                int opcao = JOptionPane.showConfirmDialog(null,
                        "Deseja deletar este cliente?", "Confirmar",
                        JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        EntityManager em = JPAUtil.getEntityManager();
                        em.getTransaction().begin();

                        Cliente cliente = em.find(Cliente.class, idSelecionado);
                        em.remove(cliente);

                        em.getTransaction().commit();
                        em.close();

                        JOptionPane.showMessageDialog(null, "Cliente deletado!");
                        limpar();
                        carregarTabela();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao deletar: " + ex.getMessage());
                    }
                }
            }
        });

        // ActionListener botao Limpar
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpar();
            }
        });

        // Selecionar linha da tabela preenche o formulario
        tabela.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                idSelecionado = (Long) modelo.getValueAt(linha, 0);
                txtNome.setText((String) modelo.getValueAt(linha, 1));
                txtEmail.setText((String) modelo.getValueAt(linha, 2));
            }
        });
    }

    private void carregarTabela() {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            List<Cliente> lista = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            em.close();

            modelo.setRowCount(0);
            for (Cliente c : lista) {
                modelo.addRow(new Object[]{c.getId(), c.getNome(), c.getEmail()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar clientes: " + e.getMessage());
        }
    }

    private void limpar() {
        txtNome.setText("");
        txtEmail.setText("");
        idSelecionado = null;
        tabela.clearSelection();
    }
}
