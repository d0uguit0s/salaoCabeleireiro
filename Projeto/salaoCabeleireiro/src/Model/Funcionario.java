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
public class Funcionario {
    private String nome;
    private String cpf;
    private String email = "";
    private String telefone;
    private Login credenciais;
    private ArrayList<Servico> servicos;

    public Funcionario(String nome, String cpf, String email, String telefone, Login credenciais, ArrayList<Servico> servicos) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.credenciais = credenciais;
        this.servicos = servicos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
