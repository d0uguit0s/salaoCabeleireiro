/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author gabri
 */
public class Cliente {
    private String nome;
    private String cpf;
    private String nasc;
    private String email;
    private String telefone;
    private Login credenciais;

    public Cliente(String nome, String cpf, String nasc, String email, String telefone, Login credenciais) {
        this.nome = nome;
        this.cpf = cpf;
        this.nasc = nasc;
        this.email = email;
        this.telefone = telefone;
        this.credenciais = credenciais;
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

    public String getNasc() {
        return nasc;
    }

    public void setNasc(String nasc) {
        this.nasc = nasc;
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
    
    
}
