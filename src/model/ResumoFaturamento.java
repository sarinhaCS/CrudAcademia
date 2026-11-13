/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author sarah
 */
public class ResumoFaturamento {
    private int totalMatriculas;
    private int qtdAtivas;
    private int qtdTrancadas;
    private int qtdCanceladas;

    private BigDecimal faturamento = BigDecimal.ZERO;    // matrículas Ativas
    private BigDecimal valorTrancadas = BigDecimal.ZERO;
    private BigDecimal valorCanceladas = BigDecimal.ZERO;
    private BigDecimal receitaMensal = BigDecimal.ZERO;  // Ativas: valor do plano / duração em meses

    // faturamento (só Ativas) agrupado por plano e por modalidade, na ordem em que aparecem
    private final Map<String, BigDecimal> porPlano = new LinkedHashMap<>();
    private final Map<String, BigDecimal> porModalidade = new LinkedHashMap<>();
    private final Map<String, Integer> qtdPorPlano = new LinkedHashMap<>();
    private final Map<String, Integer> qtdPorModalidade = new LinkedHashMap<>();

    public ResumoFaturamento(List<Matricula> matriculas) {
        for (Matricula m : matriculas) {
            BigDecimal valor = m.getValorPlano() != null ? m.getValorPlano() : BigDecimal.ZERO;
            totalMatriculas++;

            String status = m.getStatusMat() != null ? m.getStatusMat() : "";
            switch (status) {
                case "Ativa":
                    qtdAtivas++;
                    faturamento = faturamento.add(valor);
                    if (m.getDuracaoMeses() > 0) {
                        receitaMensal = receitaMensal.add(
                                valor.divide(BigDecimal.valueOf(m.getDuracaoMeses()), 2, RoundingMode.HALF_UP));
                    }
                    somar(porPlano, qtdPorPlano, m.getNomePlano(), valor);
                    somar(porModalidade, qtdPorModalidade, m.getNomeModalidade(), valor);
                    break;
                case "Trancada":
                    qtdTrancadas++;
                    valorTrancadas = valorTrancadas.add(valor);
                    break;
                case "Cancelada":
                    qtdCanceladas++;
                    valorCanceladas = valorCanceladas.add(valor);
                    break;
                default:
                    break;
            }
        }
    }

    private void somar(Map<String, BigDecimal> valores, Map<String, Integer> quantidades,
                       String chave, BigDecimal valor) {
        valores.merge(chave, valor, BigDecimal::add);
        quantidades.merge(chave, 1, Integer::sum);
    }

    // Ticket médio = faturamento / matrículas ativas
    public BigDecimal getTicketMedio() {
        if (qtdAtivas == 0) return BigDecimal.ZERO;
        return faturamento.divide(BigDecimal.valueOf(qtdAtivas), 2, RoundingMode.HALF_UP);
    }

    public int getTotalMatriculas() { return totalMatriculas; }
    public int getQtdAtivas() { return qtdAtivas; }
    public int getQtdTrancadas() { return qtdTrancadas; }
    public int getQtdCanceladas() { return qtdCanceladas; }
    public BigDecimal getFaturamento() { return faturamento; }
    public BigDecimal getValorTrancadas() { return valorTrancadas; }
    public BigDecimal getValorCanceladas() { return valorCanceladas; }
    public BigDecimal getReceitaMensal() { return receitaMensal; }
    public Map<String, BigDecimal> getPorPlano() { return porPlano; }
    public Map<String, BigDecimal> getPorModalidade() { return porModalidade; }
    public Map<String, Integer> getQtdPorPlano() { return qtdPorPlano; }
    public Map<String, Integer> getQtdPorModalidade() { return qtdPorModalidade; }
}
