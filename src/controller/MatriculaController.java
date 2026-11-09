/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.MatriculaDAO;
import model.Matricula;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author maria.gomes.s
 */
public class MatriculaController {
    
    private MatriculaDAO matriculaDAO;

    public MatriculaController() {
        this.matriculaDAO = new MatriculaDAO();
    }

    public List<Matricula> listarMatriculas() {
        try {
            return matriculaDAO.listarMatriculasComJoin();
        } catch (SQLException e) {
            System.err.println("Erro ao listar matrículas: " + e.getMessage());
            return null;
        }
    }

    public void salvar(int idAluno, int idPlano, int idMod, Integer idTutor, String dataStr, String status) throws SQLException, IllegalArgumentException {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            throw new IllegalArgumentException("A data da matrícula não pode estar vazia.");
        }

        LocalDate data;
        try {
            // Assume formato DD/MM/AAAA ou AAAA-MM-DD
            if (dataStr.contains("/")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                data = LocalDate.parse(dataStr, formatter);
            } else {
                data = LocalDate.parse(dataStr);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data inválido. Use AAAA-MM-DD ou DD/MM/YYYY.");
        }

        Matricula m = new Matricula();
        m.setIdAluno(idAluno);
        m.setIdPlano(idPlano);
        m.setIdMod(idMod);
        m.setIdTutor(idTutor);
        m.setDataMatricula(data);
        m.setStatusMat(status);

        matriculaDAO.inserir(m);
    }

    public void excluir(int idMat) throws SQLException {
        matriculaDAO.excluir(idMat);
    }
}
