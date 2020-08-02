/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Calendar;
import java.util.Date;


/**
 *
 * @author gabri
 */
public class Produto {
    private String nome;
    private double valor;
    private String validade;

    public Produto(String nome, double valor, String validade) {
        this.nome = nome;
        this.valor = valor;
        this.validade = validade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome_prod(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }
    
    
}
