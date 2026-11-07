/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import model.Plano;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanoDAO {

    // CREATE
    public void inserir(Plano p) throws SQLException {
        String sql = "INSERT INTO plano (nome_plano, valor, duracao_meses) VALUES (?, ?, ?)";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNomePlano());
            ps.setBigDecimal(2, p.getValor());
            ps.setInt(3, p.getDuracaoMeses());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    // READ (todos)
    public List<Plano> listar() throws SQLException {
        return consultar("SELECT * FROM plano ORDER BY duracao_meses", null);
    }

    // READ (filtro por nome)
    public List<Plano> buscarPorNome(String termo) throws SQLException {
        return consultar("SELECT * FROM plano WHERE nome_plano LIKE ? ORDER BY duracao_meses", "%" + termo + "%");
    }

    // UPDATE
    public void atualizar(Plano p) throws SQLException {
        String sql = "UPDATE plano SET nome_plano = ?, valor = ?, duracao_meses = ? WHERE id_plano = ?";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNomePlano());
            ps.setBigDecimal(2, p.getValor());
            ps.setInt(3, p.getDuracaoMeses());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM plano WHERE id_plano = ?";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Plano> consultar(String sql, String parametro) throws SQLException {
        List<Plano> lista = new ArrayList<>();
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Plano(
                        rs.getInt("id_plano"),
                        rs.getString("nome_plano"),
                        rs.getBigDecimal("valor"),
                        rs.getInt("duracao_meses")));
                }
            }
        }
        return lista;
    }
}