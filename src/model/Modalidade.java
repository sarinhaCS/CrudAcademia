/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author maria.gomes.s
 */
public class Modalidade {
    
    private int idMod;
    private String nome;

    // Construtor vazio
    public Modalidade() {
    }

    // Construtor com parâmetros
    public Modalidade(int idMod, String nome) {
        this.idMod = idMod;
        this.nome = nome;
    }

    // Getters e Setters
    public int getIdMod() {
        return idMod;
    }

    public void setIdMod(int idMod) {
        this.idMod = idMod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Opcional, mas muito útil para exibir o nome na JComboBox mais tarde
    @Override
    public String toString() {
        return this.nome;
    }
}