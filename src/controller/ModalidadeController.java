/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.ModalidadeDAO;
import model.Modalidade;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author maria.gomes.s
 */
public class ModalidadeController {
    
    private ModalidadeDAO modalidadeDAO;

    public ModalidadeController() {
        this.modalidadeDAO = new ModalidadeDAO();
    }

    // Método para salvar uma modalidade
    public void salvar(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da modalidade não pode estar vazio.");
        }
        
        Modalidade m = new Modalidade();
        m.setNome(nome.trim());
        modalidadeDAO.inserir(m);
    }

    // Método para listar todas as modalidades
    public List<Modalidade> listar() {
        try {
            return modalidadeDAO.listar();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para excluir uma modalidade pelo ID
    public void excluir(int idMod) throws SQLException {
        modalidadeDAO.excluir(idMod);
    }
}