/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.MatriculaDAO;     // já tem o método que faz o JOIN das 5 tabelas
import view.RelatorioView;   // a tela do relatório

import java.sql.SQLException;

/**
 * Controller do relatório de matrículas: busca os dados no DAO e manda a tela exibi-los.
 * Não tem validação porque o relatório não recebe nada digitado pelo usuário.
 */
public class RelatorioController {

    private final RelatorioView view;
    private final MatriculaDAO dao;

    // View e DAO chegam por parâmetro, igual aos outros controllers (Aluno, Instrutor, Plano).
    public RelatorioController(RelatorioView view, MatriculaDAO dao) {
        this.view = view;
        this.dao = dao;

        carregar();   // assim que a tela abre, já mostra o relatório
    }

    private void carregar() {
        try {
            // listarMatriculasComJoin() = matrícula + aluno + plano + modalidade + instrutor (LEFT JOIN)
            view.exibirMatriculas(dao.listarMatriculasComJoin());
        } catch (SQLException ex) {
            view.mostrarErro("Erro ao gerar o relatório: " + ex.getMessage());
        }
    }
}