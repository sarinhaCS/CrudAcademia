/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.Modalidade;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author maria.gomes.s
 */
public class ModalidadeDAO extends ConexaoMySQL{
    private void conectar() throws SQLException {
        try {
            abrirBanco();
        } catch (RuntimeException e) {
            throw new SQLException("Não foi possível conectar ao banco: " + e.getMessage(), e);
        }
        if (con == null) {
            throw new SQLException("Conexão não estabelecida. Verifique o driver e os dados de acesso.");
        }
    }

    // Método para Inserir / Salvar uma nova modalidade
    public void inserir(Modalidade modalidade) throws SQLException {
        String sql = "INSERT INTO modalidade (nome) VALUES (?)";
        
        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, modalidade.getNome());
            stmt.executeUpdate();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    // Método para Listar todas as modalidades cadastradas
    public List<Modalidade> listar() throws SQLException {
        List<Modalidade> lista = new ArrayList<>();
        String sql = "SELECT id_mod, nome FROM modalidade ORDER BY nome";
        
        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Modalidade m = new Modalidade();
                m.setIdMod(rs.getInt("id_mod"));
                m.setNome(rs.getString("nome"));
                lista.add(m);
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return lista;
    }

    // Método para Excluir uma modalidade pelo ID
    public void excluir(int idMod) throws SQLException {
        String sql = "DELETE FROM modalidade WHERE id_mod = ?";
        
        conectar();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idMod);
            stmt.executeUpdate();
        } finally {
            if (con != null) {
                con.close();
            }
        }
    }
}

