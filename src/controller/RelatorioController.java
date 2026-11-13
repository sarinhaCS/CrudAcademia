/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.MatriculaDAO;
import model.Matricula;
import model.ResumoFaturamento;
import view.RelatorioView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

/**
 * Controller do relatório de matrículas: lê os filtros da tela, busca os dados no DAO
 * (JOIN das 5 tabelas), calcula o faturamento e manda a tela exibir.
 */
public class RelatorioController {

    private static final DateTimeFormatter ENTRADA_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final RelatorioView view;
    private final MatriculaDAO dao;

    // View e DAO chegam por parâmetro, igual aos outros controllers (Aluno, Instrutor, Plano).
    public RelatorioController(RelatorioView view, MatriculaDAO dao) {
        this.view = view;
        this.dao = dao;

        view.addFiltrarListener(e -> carregar());
        view.addLimparListener(e -> {
            view.limparFiltros();
            carregar();
        });

        carregar();   // assim que a tela abre, já mostra o relatório completo
    }

    private void carregar() {
        LocalDate inicio;
        LocalDate fim;
        try {
            inicio = lerData(view.getDataInicio(), "inicial");
            fim = lerData(view.getDataFim(), "final");
        } catch (IllegalArgumentException ex) {
            view.mostrarErro(ex.getMessage());
            return;
        }
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            view.mostrarErro("A data inicial não pode ser depois da data final.");
            return;
        }

        try {
            List<Matricula> matriculas = dao.listarPorFiltro(inicio, fim, view.getStatus());
            view.exibirMatriculas(matriculas);
            view.exibirResumo(new ResumoFaturamento(matriculas));
        } catch (SQLException ex) {
            view.mostrarErro("Erro ao gerar o relatório: " + ex.getMessage());
        }
    }

    // Campo vazio = sem limite (devolve null). Formato esperado: dd/mm/aaaa.
    private LocalDate lerData(String texto, String qual) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return LocalDate.parse(texto.trim(), ENTRADA_DATA);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Data " + qual + " inválida. Use o formato dd/mm/aaaa (ex.: 01/10/2026).");
        }
    }
}
