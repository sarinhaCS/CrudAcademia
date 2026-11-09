/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author maria.gomes.s
 */
public class Matricula {

private int id;
    private int idAluno;
    private int idPlano;
    private int idMod;
    private Integer idTutor; // Pode ser null conforme o DDL
    private LocalDate dataMatricula;
    private String statusMat;
    
    // Atributos para o JOIN
private String nomeAluno;
    private String nomePlano;
    private String nomeModalidade;
    private String nomeInstrutor;

    public Matricula() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAluno() { return idAluno; }
    public void setIdAluno(int idAluno) { this.idAluno = idAluno; }

    public int getIdPlano() { return idPlano; }
    public void setIdPlano(int idPlano) { this.idPlano = idPlano; }

    public int getIdMod() { return idMod; }
    public void setIdMod(int idMod) { this.idMod = idMod; }

    public Integer getIdTutor() { return idTutor; }
    public void setIdTutor(Integer idTutor) { this.idTutor = idTutor; }

    public LocalDate getDataMatricula() { return dataMatricula; }
    public void setDataMatricula(LocalDate dataMatricula) { this.dataMatricula = dataMatricula; }

    public String getStatusMat() { return statusMat; }
    public void setStatusMat(String statusMat) { this.statusMat = statusMat; }

    public String getNomeAluno() { return nomeAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }

    public String getNomePlano() { return nomePlano; }
    public void setNomePlano(String nomePlano) { this.nomePlano = nomePlano; }

    public String getNomeModalidade() { return nomeModalidade; }
    public void setNomeModalidade(String nomeModalidade) { this.nomeModalidade = nomeModalidade; }

    public String getNomeInstrutor() { return nomeInstrutor; }
    public void setNomeInstrutor(String nomeInstrutor) { this.nomeInstrutor = nomeInstrutor; }
}