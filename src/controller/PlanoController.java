/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;

import dao.PlanoDAO;
import model.Plano;
import view.PlanoView;

import java.math.BigDecimal;
import java.sql.SQLException;

public class PlanoController {
    private static final BigDecimal VALOR_MAXIMO = new BigDecimal("999999.99");   // limite do DECIMAL(8,2)

    private final PlanoView view;
    private final PlanoDAO dao;

    public PlanoController(PlanoView view, PlanoDAO dao) {
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
            view.exibirPlanos(dao.listar());
        } catch (SQLException ex) {
            view.mostrarErro("Erro ao listar planos: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            String termo = view.getBusca();
            view.exibirPlanos(termo.isEmpty() ? dao.listar() : dao.buscarPorNome(termo));
        } catch (SQLException ex) {
            view.mostrarErro("Erro na busca: " + ex.getMessage());
        }
    }

    private void salvar() {
        Plano p = lerFormulario();
        if (p == null) return;
        try {
            dao.inserir(p);
            view.mostrarMensagem("Plano cadastrado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void alterar() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um plano na tabela.");
            return;
        }
        Plano p = lerFormulario();
        if (p == null) return;
        p.setId(id);
        try {
            dao.atualizar(p);
            view.mostrarMensagem("Plano atualizado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void excluir() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um plano na tabela.");
            return;
        }
        if (!view.confirmar("Deseja realmente excluir este plano?")) return;
        try {
            dao.excluir(id);
            view.mostrarMensagem("Plano excluído com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    /** Valida os campos e monta o objeto Plano; devolve null se algo estiver inválido. */
    private Plano lerFormulario() {
        String nome = view.getNome();
        if (nome.isEmpty()) { view.mostrarErro("Informe o nome do plano."); return null; }
        if (nome.length() > 50) { view.mostrarErro("Nome muito longo (máx. 50)."); return null; }

        BigDecimal valor = lerValor(view.getValor());
        if (valor == null) return null;

        int duracao;
        try {
            duracao = Integer.parseInt(view.getDuracao());
        } catch (NumberFormatException ex) {
            view.mostrarErro("A duração deve ser um número inteiro de meses.");
            return null;
        }
        if (duracao <= 0 || duracao > 120) {
            view.mostrarErro("A duração deve estar entre 1 e 120 meses.");
            return null;
        }

        Plano p = new Plano();
        p.setNomePlano(nome);
        p.setValor(valor);
        p.setDuracaoMeses(duracao);
        return p;
    }

    /** Aceita "119,90", "119.90", "1.099,90" ou "R$ 119,90". Devolve null (e avisa) se inválido. */
    private BigDecimal lerValor(String texto) {
        String t = texto.replace("R$", "").replace(" ", "");
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", ".");   // formato brasileiro
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(t);
        } catch (NumberFormatException ex) {
            view.mostrarErro("Valor inválido. Exemplo: 119,90");
            return null;
        }
        if (valor.signum() <= 0) {
            view.mostrarErro("O valor deve ser maior que zero.");
            return null;
        }
        if (valor.stripTrailingZeros().scale() > 2) {
            view.mostrarErro("O valor deve ter no máximo 2 casas decimais.");
            return null;
        }
        if (valor.compareTo(VALOR_MAXIMO) > 0) {
            view.mostrarErro("Valor acima do limite permitido (999999,99).");
            return null;
        }
        return valor.setScale(2);
    }

    private void tratarErroSQL(SQLException ex) {
        switch (ex.getErrorCode()) {
            case 1451 -> view.mostrarErro("Não é possível excluir: o plano está sendo usado em matrículas.\n"
                                        + "Altere ou remova essas matrículas antes.");
            default -> view.mostrarErro("Erro de banco de dados: " + ex.getMessage());
        }
    }
}