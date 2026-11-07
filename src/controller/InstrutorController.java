/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.InstrutorDAO;
import model.Instrutor;
import view.InstrutorView;

import java.sql.SQLException;

public class InstrutorController {
    private final InstrutorView view;
    private final InstrutorDAO dao;

    public InstrutorController(InstrutorView view, InstrutorDAO dao) {
        this.view = view;
        this.dao = dao;

        view.addSalvarListener(e -> salvar());
        view.addAlterarListener(e -> alterar());
        view.addExcluirListener(e -> excluir());
        view.addLimparListener(e -> view.limparCampos());
        view.addBuscarListener(e -> buscar());

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            view.exibirInstrutores(dao.listar());
        } catch (SQLException ex) {
            view.mostrarErro("Erro ao listar instrutores: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            String termo = view.getBusca();
            view.exibirInstrutores(termo.isEmpty() ? dao.listar() : dao.buscarPorNome(termo));
        } catch (SQLException ex) {
            view.mostrarErro("Erro na busca: " + ex.getMessage());
        }
    }

    private void salvar() {
        Instrutor i = lerFormulario();
        if (i == null) return;
        try {
            dao.inserir(i);
            view.mostrarMensagem("Instrutor cadastrado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void alterar() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um instrutor na tabela.");
            return;
        }
        Instrutor i = lerFormulario();
        if (i == null) return;
        i.setId(id);
        try {
            dao.atualizar(i);
            view.mostrarMensagem("Instrutor atualizado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void excluir() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um instrutor na tabela.");
            return;
        }
        if (!view.confirmar("Deseja realmente excluir este instrutor?")) return;
        try {
            dao.excluir(id);
            view.mostrarMensagem("Instrutor excluído com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private Instrutor lerFormulario() {
        String nome = view.getNome();
        String cref = view.getCref().toUpperCase();

        if (nome.isEmpty()) { view.mostrarErro("Informe o nome."); return null; }
        if (nome.length() > 200) { view.mostrarErro("Nome muito longo (máx. 200)."); return null; }
        if (cref.isEmpty()) { view.mostrarErro("Informe o CREF."); return null; }
        if (cref.length() > 20) { view.mostrarErro("CREF muito longo (máx. 20)."); return null; }

        Instrutor i = new Instrutor();
        i.setNome(nome);
        i.setCref(cref);
        return i;
    }

    private void tratarErroSQL(SQLException ex) {
        switch (ex.getErrorCode()) {
            case 1062 -> view.mostrarErro("Já existe um instrutor cadastrado com este CREF.");
            case 1451 -> view.mostrarErro("Não é possível excluir: o instrutor está vinculado a matrículas.\n"
                                        + "Altere ou remova essas matrículas antes.");
            default -> view.mostrarErro("Erro de banco de dados: " + ex.getMessage());
        }
    }
}