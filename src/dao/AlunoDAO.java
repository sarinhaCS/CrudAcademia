/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // CREATE
    public void inserir(Aluno a) throws SQLException {
        String sql = "INSERT INTO aluno (nome, cpf, telefone, data_nascimento) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getCpf());
            ps.setString(3, a.getTelefone());
            ps.setDate(4, Date.valueOf(a.getDataNascimento()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
        }
    }

    // READ (todos)
    public List<Aluno> listar() throws SQLException {
        return consultar("SELECT * FROM aluno ORDER BY nome", null);
    }

    // READ (filtro por nome)
    public List<Aluno> buscarPorNome(String termo) throws SQLException {
        return consultar("SELECT * FROM aluno WHERE nome LIKE ? ORDER BY nome", "%" + termo + "%");
    }

    // UPDATE
    public void atualizar(Aluno a) throws SQLException {
        String sql = "UPDATE aluno SET nome = ?, cpf = ?, telefone = ?, data_nascimento = ? WHERE id_aluno = ?";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getCpf());
            ps.setString(3, a.getTelefone());
            ps.setDate(4, Date.valueOf(a.getDataNascimento()));
            ps.setInt(5, a.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM aluno WHERE id_aluno = ?";
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Aluno> consultar(String sql, String parametro) throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        try (Connection con = ConexaoMySQL.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Aluno(
                        rs.getInt("id_aluno"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getDate("data_nascimento").toLocalDate()));
                }
            }
        }
        return lista;
    }
}