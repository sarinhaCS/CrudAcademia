/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

 
import java.time.format.DateTimeFormatter;   // formato da data exibida
import java.util.List;                       // lista recebida do controller
import javax.swing.JOptionPane;              // caixa de mensagem de erro
import javax.swing.table.DefaultTableModel;  // modelo que guarda linhas e colunas da tabela
import model.Matricula;   

/**
 *
 * @author Maria Eduarda
 */

public class RelatorioView extends javax.swing.JFrame {
 
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RelatorioView.class.getName());
 
    // Formato usado para MOSTRAR a data na tabela (dia/mês/ano)
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 
    // "modelo" guarda as linhas e colunas da tabela
    private DefaultTableModel modelo;
   
public RelatorioView(){
        initComponents();
        setSize(950, 420);
        setLocationRelativeTo(null);   // centraliza a janela
        modelo = (DefaultTableModel) jTable1.getModel();
 
        modelo = new DefaultTableModel(
                new String[]{"ID", "Aluno", "Plano", "Modalidade", "Instrutor", "Data", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        jTable1.setModel(modelo);   // troca as colunas "Title 1..4" do designer pelas reais
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
                m.getStatusMat()});
        }
    }

    // Mostra uma caixa de erro (o controller chama quando o banco falha)
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
