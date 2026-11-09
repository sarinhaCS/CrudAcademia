/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Matricula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Matricula. Herda de ConexaoMySQL seguindo o padrão do projeto.
 */
public class MatriculaDAO extends ConexaoMySQL {

   private void conectar() throws SQLException {
        try {
            abrirBanco();
        } catch (RuntimeException e) {
            throw new SQLException("Não foi possível conectar ao banco: " + e.getMessage(), e);
        }
        if (con == null) {
            throw new SQLException("Conexão não estabelecida.");
        }
    }
// Método para Inserir uma nova matrícula
    public void inserir(Matricula m) throws SQLException {
        String sql = "INSERT INTO matricula (id_aluno, id_plano, id_mod, id_tutor, data_matricula, status_mat) VALUES (?, ?, ?, ?, ?, ?)";
        
        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, m.getIdAluno());
            stmt.setInt(2, m.getIdPlano());
            stmt.setInt(3, m.getIdMod());
            
            if (m.getIdTutor() != null) {
                stmt.setInt(4, m.getIdTutor());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            if (m.getDataMatricula() != null) {
                stmt.setDate(5, Date.valueOf(m.getDataMatricula()));
            } else {
                stmt.setNull(5, Types.DATE);
            }
            
            stmt.setString(6, m.getStatusMat());
            stmt.executeUpdate();
        } finally {
            if (con != null) con.close();
        }
    }
    
    // Método para Listar com JOINs
    public List<Matricula> listarMatriculasComJoin() throws SQLException {
        List<Matricula> matriculas = new ArrayList<>();
        
        String sql = "SELECT m.id_mat AS id, a.nome AS nome_aluno, p.nome_plano AS nome_plano, " +
                     "mo.nome AS nome_modalidade, t.nome AS nome_instrutor, " +
                     "m.data_matricula, m.status_mat " +
                     "FROM matricula m " +
                     "INNER JOIN aluno a ON m.id_aluno = a.id_aluno " +
                     "INNER JOIN plano p ON m.id_plano = p.id_plano " +
                     "INNER JOIN modalidade mo ON m.id_mod = mo.id_mod " +
                     "LEFT JOIN instrutor t ON m.id_tutor = t.id_tutor";

        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Matricula m = new Matricula();
                m.setId(rs.getInt("id"));
                m.setNomeAluno(rs.getString("nome_aluno"));
                m.setNomePlano(rs.getString("nome_plano"));
                m.setNomeModalidade(rs.getString("nome_modalidade"));
                m.setNomeInstrutor(rs.getString("nome_instrutor"));
                
                Date data = rs.getDate("data_matricula");
                if (data != null) {
                    m.setDataMatricula(data.toLocalDate());
                }
                
                m.setStatusMat(rs.getString("status_mat"));
                matriculas.add(m);
            }
        } finally {
            if (con != null) con.close();
        }
        return matriculas;
    }
    // Método para Excluir matrícula pelo ID
    public void excluir(int idMat) throws SQLException {
        String sql = "DELETE FROM matricula WHERE id_mat = ?";
        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMat);
            stmt.executeUpdate();
        } finally {
            if (con != null) con.close();
        }
    }
}