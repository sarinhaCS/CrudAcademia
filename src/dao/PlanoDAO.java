/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Plano;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Plano. Herda de ConexaoMySQL (como o restante do grupo) e usa
 * abrirBanco() e o atributo "con" herdados.
 */
public class PlanoDAO extends ConexaoMySQL {

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
    public void inserir(Plano p) throws SQLException {
        String sql = "INSERT INTO plano (nome_plano, valor, duracao_meses) VALUES (?, ?, ?)";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNomePlano());
            ps.setBigDecimal(2, p.getValor());
            ps.setInt(3, p.getDuracaoMeses());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) p.setId(chaves.getInt(1));
            }
        } finally {
            con.close();
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
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNomePlano());
            ps.setBigDecimal(2, p.getValor());
            ps.setInt(3, p.getDuracaoMeses());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM plano WHERE id_plano = ?";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    private List<Plano> consultar(String sql, String parametro) throws SQLException {
        List<Plano> lista = new ArrayList<>();
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet resultado = ps.executeQuery()) {
                while (resultado.next()) {
                    lista.add(new Plano(
                        resultado.getInt("id_plano"),
                        resultado.getString("nome_plano"),
                        resultado.getBigDecimal("valor"),
                        resultado.getInt("duracao_meses")));
                }
            }
        } finally {
            con.close();
        }
        return lista;
    }
}