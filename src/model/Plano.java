/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

public class Plano {
    private int id;                 
    private String nomePlano;       
    private BigDecimal valor;       // DECIMAL(8,2): BigDecimal evita erro de arredondamento com dinheiro
    private int duracaoMeses;       

    public Plano() {}

    public Plano(int id, String nomePlano, BigDecimal valor, int duracaoMeses) {
        this.id = id;
        this.nomePlano = nomePlano;
        this.valor = valor;
        this.duracaoMeses = duracaoMeses;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomePlano() { return nomePlano; }
    public void setNomePlano(String nomePlano) { this.nomePlano = nomePlano; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    // mostra o nome do plano
    @Override
    public String toString(){ 
        return nomePlano; 
    }
}