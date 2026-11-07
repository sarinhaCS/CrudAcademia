/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Aluno;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Aluno. Herda de ConexaoMySQL (como o restante do grupo) e usa
 * abrirBanco() e o atributo "con" herdados.
 */
public class AlunoDAO extends ConexaoMySQL {

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
    public void inserir(Aluno a) throws SQLException {
        String sql = "INSERT INTO aluno (nome, cpf, telefone, data_nascimento) VALUES (?, ?, ?, ?)";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getCpf());
            ps.setString(3, a.getTelefone());
            ps.setDate(4, java.sql.Date.valueOf(a.getDataNascimento()));
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) a.setId(chaves.getInt(1));
            }
        } finally {
            con.close();
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
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getCpf());
            ps.setString(3, a.getTelefone());
            ps.setDate(4, java.sql.Date.valueOf(a.getDataNascimento()));
            ps.setInt(5, a.getId());
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    // DELETE
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM aluno WHERE id_aluno = ?";
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } finally {
            con.close();
        }
    }

    private List<Aluno> consultar(String sql, String parametro) throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        conectar();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet resultado = ps.executeQuery()) {
                while (resultado.next()) {
                    lista.add(new Aluno(
                        resultado.getInt("id_aluno"),
                        resultado.getString("nome"),
                        resultado.getString("cpf"),
                        resultado.getString("telefone"),
                        resultado.getDate("data_nascimento").toLocalDate()));
                }
            }
        } finally {
            con.close();
        }
        return lista;
    }
}