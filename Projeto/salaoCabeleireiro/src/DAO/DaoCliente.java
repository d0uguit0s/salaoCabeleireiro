/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gabri
 */
public class DaoCliente {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    //ADICIONAR
    public void adicionar (Cliente cliente){
        String sql1 = "INSERT INTO tbl_login(usuario, senha) VALUES (?, ?);";
        String sql2 = "INSERT INTO tbl_cliente(nome_cliente, cpf_cliente, dt_nasc_cliente, email_cliente, telefone_cliente, FK_id_login) VALUES (?, ?, ?, ?, ?, (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ?));";
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, cliente.getCredenciais().getUsuario());
            stmt1.setString(2, cliente.getCredenciais().getSenha());
            stmt1.execute();
            stmt1.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, cliente.getNome());
            stmt2.setString(2, cliente.getCpf());
            stmt2.setString(3, cliente.getNasc());
            stmt2.setString(4, cliente.getEmail());
            stmt2.setString(5, cliente.getTelefone());
            stmt2.setString(6, cliente.getCredenciais().getUsuario());
            stmt2.setString(7, cliente.getCredenciais().getSenha());
            stmt2.execute();
            stmt2.close();           
            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    //CONSULTAR
    public ResultSet listar(){
        String sql = "SELECT tbl_cliente.id_cliente, tbl_cliente.nome_cliente, tbl_cliente.cpf_cliente, tbl_cliente.dt_nasc_cliente, tbl_cliente.email_cliente, tbl_cliente.telefone_cliente, tbl_login.usuario, tbl_login.senha FROM tbl_cliente\n" +
                     "JOIN tbl_login ON tbl_login.id_login = tbl_cliente.FK_id_login\n"+
                     "WHERE tbl_cliente.estatus = 'A';";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //ATUALIZAR
    public void alterar(Cliente cliente, String codigo){
        
        String sql1 = "UPDATE tbl_login SET usuario = ?, senha = ? WHERE id_login = (SELECT FK_id_login FROM tbl_cliente WHERE id_cliente = ?);";
        String sql2 = "UPDATE tbl_cliente SET nome_cliente = ?, cpf_cliente = ?, dt_nasc_cliente = ?, email_cliente = ?, telefone_cliente = ?, FK_id_login = (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ?) WHERE id_cliente = ?;";
        
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, cliente.getCredenciais().getUsuario());
            stmt1.setString(2, cliente.getCredenciais().getSenha());
            stmt1.setString(3, codigo);
            stmt1.execute();
            stmt1.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, cliente.getNome());
            stmt2.setString(2, cliente.getCpf());
            stmt2.setString(3, cliente.getNasc());
            stmt2.setString(4, cliente.getEmail());
            stmt2.setString(5, cliente.getTelefone());
            stmt2.setString(6, cliente.getCredenciais().getUsuario());
            stmt2.setString(7, cliente.getCredenciais().getSenha());
            stmt2.setString(8, codigo);
            stmt2.execute();
            stmt2.close();         
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_cliente SET estatus = 'D' WHERE id_cliente = ?;";
        String sql2 = "UPDATE tbl_login SET estatus = 'D'\n"+ 
                      "WHERE id_login = (SELECT FK_id_login FROM tbl_cliente WHERE id_cliente = ?);";
        
         try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            stmt.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, codigo);
            stmt2.execute();
            stmt2.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}


