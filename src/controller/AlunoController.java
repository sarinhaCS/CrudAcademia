/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.AlunoDAO;
import model.Aluno;
import view.AlunoView;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Controller: liga a View ao DAO e aplica as validações. */
public class AlunoController {
    private static final DateTimeFormatter ENTRADA_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final AlunoView view;
    private final AlunoDAO dao;

    public AlunoController(AlunoView view, AlunoDAO dao) {
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
            view.exibirAlunos(dao.listar());
        } catch (SQLException ex) {
            view.mostrarErro("Erro ao listar alunos: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            String termo = view.getBusca();
            view.exibirAlunos(termo.isEmpty() ? dao.listar() : dao.buscarPorNome(termo));
        } catch (SQLException ex) {
            view.mostrarErro("Erro na busca: " + ex.getMessage());
        }
    }

    private void salvar() {
        Aluno a = lerFormulario();
        if (a == null) return;
        try {
            dao.inserir(a);
            view.mostrarMensagem("Aluno cadastrado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void alterar() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um aluno na tabela.");
            return;
        }
        Aluno a = lerFormulario();
        if (a == null) return;
        a.setId(id);
        try {
            dao.atualizar(a);
            view.mostrarMensagem("Aluno atualizado com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    private void excluir() {
        Integer id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarErro("Selecione um aluno na tabela.");
            return;
        }
        if (!view.confirmar("Deseja realmente excluir este aluno?")) return;
        try {
            dao.excluir(id);
            view.mostrarMensagem("Aluno excluído com sucesso!");
            view.limparCampos();
            carregarTabela();
        } catch (SQLException ex) {
            tratarErroSQL(ex);
        }
    }

    /** Valida os campos e monta o objeto Aluno; devolve null se algo estiver inválido. */
    private Aluno lerFormulario() {
        String nome = view.getNome();
        String cpf = view.getCpf().replaceAll("\\D", "");   
        String telefone = view.getTelefone();

        if (nome.isEmpty()){
            view.mostrarErro("Informe o nome."); 
            return null; 
        }
        
        if (nome.length() > 200){ 
            view.mostrarErro("Nome muito longo (máx. 200)."); 
            return null;
        }
        
        if (cpf.length() != 11){ 
            view.mostrarErro("O CPF deve ter 11 dígitos.");
            return null; 
        }
        
        if (telefone.length() > 20){ 
            view.mostrarErro("Telefone muito longo (máx. 20)."); 
            return null; 
        }
        
        LocalDate nascimento;
        try {
            nascimento = LocalDate.parse(view.getNascimento(), ENTRADA_DATA);
        } catch (DateTimeParseException ex) {
            view.mostrarErro("Data inválida. Use o formato dd/mm/aaaa.");
            return null;
        }
        if (nascimento.isAfter(LocalDate.now())) {
            view.mostrarErro("A data de nascimento não pode ser futura.");
            return null;
        }

        Aluno a = new Aluno();
        a.setNome(nome);
        a.setCpf(cpf);
        a.setTelefone(telefone.isEmpty() ? null : telefone); 
        a.setDataNascimento(nascimento);
        return a;
    }

    private void tratarErroSQL(SQLException ex) {
        switch (ex.getErrorCode()) {
            case 1062 -> view.mostrarErro("Já existe um aluno cadastrado com este CPF.");
            case 1451 -> view.mostrarErro("Não é possível excluir: o aluno possui matrículas.\n"
                                        + "Remova ou cancele as matrículas dele antes.");
            default -> view.mostrarErro("Erro de banco de dados: " + ex.getMessage());
        }
    }
}