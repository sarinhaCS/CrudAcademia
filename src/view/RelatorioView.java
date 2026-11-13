/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.Matricula;
import model.ResumoFaturamento;

/**
 * Tela do relatório de matrículas: filtros (período e situação), tabela com os NOMES
 * (aluno, plano, modalidade, instrutor) e resumo de faturamento. Só desenha a tela;
 * quem busca os dados e calcula é o RelatorioController.
 *
 * @author Victor Gabriel
 */

public class RelatorioView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RelatorioView.class.getName());

    // Formato usado para MOSTRAR a data na tabela (dia/mês/ano)
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Formato de dinheiro: R$ 1.099,90
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    // tabela principal (matrículas)
    private DefaultTableModel modelo;

    // filtros
    private JTextField txtInicio;
    private JTextField txtFim;
    private JComboBox<String> cmbStatus;
    private JButton btnFiltrar;
    private JButton btnLimpar;

    // resumo de faturamento
    private JLabel lblFaturamento;
    private JLabel lblReceitaMensal;
    private JLabel lblTicketMedio;
    private JLabel lblAtivas;
    private JLabel lblTrancadas;
    private JLabel lblCanceladas;
    private DefaultTableModel modeloPlano;
    private DefaultTableModel modeloModalidade;

    public RelatorioView() {
        initComponents();
        // fechar o relatório NÃO pode encerrar o sistema inteiro (o designer deixa EXIT_ON_CLOSE)
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatório de Matrículas e Faturamento");

        modelo = new DefaultTableModel(
                new String[]{"ID", "Aluno", "Plano", "Modalidade", "Instrutor", "Data", "Status", "Valor do plano"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        jTable1.setModel(modelo);   // troca as colunas do designer pelas reais

        getContentPane().add(criarPainelFiltros(), BorderLayout.NORTH);
        getContentPane().add(criarPainelResumo(), BorderLayout.SOUTH);

        setSize(1050, 700);
        setLocationRelativeTo(null);   // centraliza a janela
    }

    // ---------- montagem da tela ----------

    private JPanel criarPainelFiltros() {
        JPanel p = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder("Filtros"));

        txtInicio = new JTextField(8);
        txtFim = new JTextField(8);
        cmbStatus = new JComboBox<>(new String[]{"Todas", "Ativa", "Trancada", "Cancelada"});
        btnFiltrar = new JButton("Filtrar");
        btnLimpar = new JButton("Limpar");

        p.add(new JLabel("De (dd/mm/aaaa):"));
        p.add(txtInicio);
        p.add(new JLabel("Até:"));
        p.add(txtFim);
        p.add(new JLabel("Situação:"));
        p.add(cmbStatus);
        p.add(btnFiltrar);
        p.add(btnLimpar);
        return p;
    }

    private JPanel criarPainelResumo() {
        JPanel painel = new JPanel(new BorderLayout(0, 6));
        painel.setBorder(BorderFactory.createTitledBorder("Faturamento da academia (matrículas do filtro)"));

        // linha de "cards" com os totais
        lblFaturamento = new JLabel("-");
        lblReceitaMensal = new JLabel("-");
        lblTicketMedio = new JLabel("-");
        lblAtivas = new JLabel("-");
        lblTrancadas = new JLabel("-");
        lblCanceladas = new JLabel("-");

        JPanel cards = new JPanel(new GridLayout(1, 6, 6, 0));
        cards.add(criarCard("Faturamento (ativas)", lblFaturamento, new Color(0, 110, 40)));
        cards.add(criarCard("Receita mensal estimada", lblReceitaMensal, new Color(0, 110, 40)));
        cards.add(criarCard("Ticket médio", lblTicketMedio, Color.DARK_GRAY));
        cards.add(criarCard("Ativas", lblAtivas, Color.DARK_GRAY));
        cards.add(criarCard("Trancadas", lblTrancadas, new Color(190, 120, 0)));
        cards.add(criarCard("Canceladas", lblCanceladas, new Color(170, 0, 0)));
        painel.add(cards, BorderLayout.NORTH);

        // duas tabelinhas: faturamento por plano e por modalidade
        modeloPlano = novoModeloResumo("Plano");
        modeloModalidade = novoModeloResumo("Modalidade");
        JPanel tabelas = new JPanel(new GridLayout(1, 2, 8, 0));
        tabelas.add(criarTabelaResumo("Faturamento por plano", modeloPlano));
        tabelas.add(criarTabelaResumo("Faturamento por modalidade", modeloModalidade));
        tabelas.setPreferredSize(new java.awt.Dimension(100, 150));
        painel.add(tabelas, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarCard(String titulo, JLabel valor, Color cor) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setFont(t.getFont().deriveFont(Font.PLAIN, 11f));
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        valor.setFont(valor.getFont().deriveFont(Font.BOLD, 16f));
        valor.setForeground(cor);
        card.add(t);
        card.add(valor);
        return card;
    }

    private DefaultTableModel novoModeloResumo(String primeiraColuna) {
        return new DefaultTableModel(new String[]{primeiraColuna, "Matrículas ativas", "Faturamento"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JScrollPane criarTabelaResumo(String titulo, DefaultTableModel m) {
        JScrollPane sp = new JScrollPane(new JTable(m));
        sp.setBorder(BorderFactory.createTitledBorder(titulo));
        return sp;
    }

    // ---------- métodos usados pelo Controller ----------

    public String getDataInicio() { return txtInicio.getText().trim(); }
    public String getDataFim() { return txtFim.getText().trim(); }

    // devolve null quando está em "Todas" (sem filtro de situação)
    public String getStatus() {
        String s = (String) cmbStatus.getSelectedItem();
        return "Todas".equals(s) ? null : s;
    }

    public void addFiltrarListener(ActionListener l) { btnFiltrar.addActionListener(l); }
    public void addLimparListener(ActionListener l) { btnLimpar.addActionListener(l); }

    public void limparFiltros() {
        txtInicio.setText("");
        txtFim.setText("");
        cmbStatus.setSelectedIndex(0);
    }

    // Recebe do controller a lista de matrículas (já com JOIN) e coloca na tabela.
    public void exibirMatriculas(List<Matricula> matriculas) {
        modelo.setRowCount(0);   // apaga as linhas atuais
        for (Matricula m : matriculas) {
            modelo.addRow(new Object[]{
                m.getId(),
                m.getNomeAluno(),
                m.getNomePlano(),
                m.getNomeModalidade(),
                // o instrutor é opcional: sem instrutor o banco devolve null (LEFT JOIN)
                m.getNomeInstrutor() != null ? m.getNomeInstrutor() : "Sem instrutor",
                m.getDataMatricula() != null ? m.getDataMatricula().format(FORMATO_DATA) : "",
                m.getStatusMat(),
                moeda(m.getValorPlano())});
        }
    }

    // Recebe o resumo já calculado e preenche os cards e as tabelinhas de faturamento.
    public void exibirResumo(ResumoFaturamento r) {
        lblFaturamento.setText(moeda(r.getFaturamento()));
        lblReceitaMensal.setText(moeda(r.getReceitaMensal()));
        lblTicketMedio.setText(moeda(r.getTicketMedio()));
        lblAtivas.setText(String.valueOf(r.getQtdAtivas()));
        lblTrancadas.setText(r.getQtdTrancadas() + " (" + moeda(r.getValorTrancadas()) + ")");
        lblCanceladas.setText(r.getQtdCanceladas() + " (" + moeda(r.getValorCanceladas()) + ")");
        preencher(modeloPlano, r.getPorPlano(), r.getQtdPorPlano());
        preencher(modeloModalidade, r.getPorModalidade(), r.getQtdPorModalidade());
    }

    private void preencher(DefaultTableModel m, Map<String, BigDecimal> valores, Map<String, Integer> qtds) {
        m.setRowCount(0);
        for (Map.Entry<String, BigDecimal> e : valores.entrySet()) {
            m.addRow(new Object[]{e.getKey(), qtds.get(e.getKey()), moeda(e.getValue())});
        }
    }

    private String moeda(BigDecimal v) {
        return v == null ? "" : MOEDA.format(v);
    }

    // Mostra uma caixa de erro (o controller chama quando o banco falha ou a data é inválida)
    public void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("unchecked") 
 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Aluno", "Plano", "Modalidade", "Instrutor", "Data", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new RelatorioView().setVisible(true));
    }
} 
