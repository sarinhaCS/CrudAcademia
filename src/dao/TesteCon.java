/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.SQLException;

/**
 *
 * @author sarah
 */
public class TesteCon {
    public static void main(String[] args) throws SQLException {
        ConexaoMySQL conexao = new ConexaoMySQL();
        conexao.abrirBanco();
    }
}
