/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import controller.AlunoController;
import controller.InstrutorController;
import controller.PlanoController;
import dao.AlunoDAO;
import dao.InstrutorDAO;
import dao.PlanoDAO;
import view.AlunoView;
import view.InstrutorView;
import view.PlanoView;

import javax.swing.SwingUtilities;

/**
 * Main provisório, só para testar os CRUDs enquanto o MenuPrincipal não fica pronto.
 * Quando o menu existir, este método deve apenas abrir o MenuPrincipal.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AlunoView alunoView = new AlunoView();
            new AlunoController(alunoView, new AlunoDAO());
            alunoView.setLocation(40, 40);
            alunoView.setVisible(true);

            InstrutorView instrutorView = new InstrutorView();
            new InstrutorController(instrutorView, new InstrutorDAO());
            instrutorView.setLocation(90, 90);
            instrutorView.setVisible(true);

            PlanoView planoView = new PlanoView();
            new PlanoController(planoView, new PlanoDAO());
            planoView.setLocation(140, 140);
            planoView.setVisible(true);
        });
    }
}