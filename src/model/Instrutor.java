/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Instrutor {
    private int id;    
    private String nome;
    private String cref;

    public Instrutor() {}

    public Instrutor(int id, String nome, String cref) {
        this.id = id;
        this.nome = nome;
        this.cref = cref;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCref() { return cref; }
    public void setCref(String cref) { this.cref = cref; }

    // o JComboBox da matrícula mostra o nome do instrutor
    @Override
    public String toString() { return nome; }
}