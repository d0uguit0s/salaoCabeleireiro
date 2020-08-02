/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class Venda {
    private double totalVendas;
    private String codigoAgendamento;
    private ArrayList<Produto> produtos;

    public Venda(double totalVendas, String codigoAgendamento, ArrayList<Produto> produtos) {
        this.totalVendas = totalVendas;
        this.codigoAgendamento = codigoAgendamento;
        this.produtos = produtos;
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public String getCodigoAgendamento() {
        return codigoAgendamento;
    }

    public void setCodigoAgendamento(String codigoAgendamento) {
        this.codigoAgendamento = codigoAgendamento;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }
    
    
}
