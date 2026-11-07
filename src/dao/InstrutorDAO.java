/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Instrutor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Instrutor. Herda de ConexaoMySQL (como o restante do grupo) e usa
 * abrirBanco() e o atributo "con" herdados.
 */
public class InstrutorDAO extends ConexaoMySQL {

    // abre a conexão herdada e converte falhas em SQLException (o Controller já trata)
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

    // CREATE
    public void inserir(Instrutor i) throws SQLException {
        String sql = "INSERT INTO instrutor (nome, cref) VALUES (?, ?)";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCref());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) i.setId(chaves.getInt(1));
            }
        } finally {
            con.close();
        }
    }

    // READ (todos)
    public List<Instrutor> listar() throws SQLException {
        return consultar("SELECT * FROM instrutor ORDER BY nome", null);
    }

    // READ (filtro por nome)
    public List<Instrutor> buscarPorNome(String termo) throws SQLException {
        return consultar("SELECT * FROM instrutor WHERE nome LIKE ? ORDER BY nome", "%" + termo + "%");
    }

    // UPDATE
    public void atualizar(Instrutor i) throws SQLException {
        String sql = "UPDATE instrutor SET nome = ?, cref = ? WHERE id_tutor = ?";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCref());
            ps.setInt(3, i.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM instrutor WHERE id_tutor = ?";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    private List<Instrutor> consultar(String sql, String parametro) throws SQLException {
        List<Instrutor> lista = new ArrayList<>();
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet resultado = ps.executeQuery()) {
                while (resultado.next()) {
                    lista.add(new Instrutor(
                        resultado.getInt("id_tutor"),
                        resultado.getString("nome"),
                        resultado.getString("cref")));
                }
            }
        } finally {
            con.close();
        }
        return lista;
    }
}