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
public class Agendamento {
    private Login credenciais;
    private String data;
    private String hora;
    private ArrayList<Servico> servicos;

    public Agendamento(Login credenciais, String data, String hora, ArrayList<Servico> servicos) {
        this.credenciais = credenciais;
        this.data = data;
        this.hora = hora;
        this.servicos = servicos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Login getCredenciais() {
        return credenciais;
    }

    public void setCredenciais(Login credenciais) {
        this.credenciais = credenciais;
    }

    public ArrayList<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(ArrayList<Servico> servicos) {
        this.servicos = servicos;
    }
    
    
}
