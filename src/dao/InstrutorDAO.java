/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Instrutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstrutorDAO {

    // CREATE
    public void inserir(Instrutor i) throws SQLException {
        String sql = "INSERT INTO instrutor (nome, cref) VALUES (?, ?)";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCref());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) i.setId(rs.getInt(1));
            }
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
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getNome());
            ps.setString(2, i.getCref());
            ps.setInt(3, i.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM instrutor WHERE id_tutor = ?";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Instrutor> consultar(String sql, String parametro) throws SQLException {
        List<Instrutor> lista = new ArrayList<>();
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Instrutor(
                        rs.getInt("id_tutor"),
                        rs.getString("nome"),
                        rs.getString("cref")));
                }
            }
        }
        return lista;
    }
}