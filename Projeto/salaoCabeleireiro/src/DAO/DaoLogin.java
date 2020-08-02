/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Douglas
 */
public class DaoLogin {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    public boolean isCliente(Login login){
        boolean isCliente = false;
        String sql = "SELECT * FROM tbl_cliente\n" +
                     "JOIN tbl_login ON tbl_login.id_login = tbl_cliente.FK_id_login\n" +
                     "WHERE FK_id_login = (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ? AND tbl_login.estatus = 'A') AND tbl_cliente.estatus = 'A';";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            stmt.setString(2, login.getSenha());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                isCliente = true;
            }            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        return isCliente;
    }
    
    public boolean isFuncionario(Login login){
        boolean isFuncionario = false;
        String sql = "SELECT * FROM tbl_funcionario\n" +
                     "JOIN tbl_login ON tbl_login.id_login = tbl_funcionario.FK_id_login\n" +
                     "WHERE FK_id_login = (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ? AND tbl_login.estatus = 'A') AND tbl_funcionario.estatus = 'A';";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            stmt.setString(2, login.getSenha());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                isFuncionario = true;
            }            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        return isFuncionario;
    }
    
    public boolean validar (Login login){
        boolean loginValido = false;
        String sql = "SELECT usuario, senha FROM tbl_login WHERE usuario = ? AND senha = ? AND estatus = 'A';";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            stmt.setString(2, login.getSenha());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                loginValido = true;
            }            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        return loginValido;
    }
    
    public ArrayList<String> getInfoFuncionario(Login login){
        String sql = "SELECT nome_funcionario, id_funcionario FROM tbl_login\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.FK_id_login = tbl_login.id_login\n" +
                     "WHERE usuario = ? AND senha = ? AND tbl_funcionario.estatus = 'A';";
        ArrayList<String> dados = new ArrayList<>();
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            stmt.setString(2, login.getSenha());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                dados.add(rs.getString(1));
                dados.add(rs.getString(2));
                //System.out.println(rs.getString(2));
            }            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        return dados;
    }
    
    public ArrayList<String> getNomeCliente(Login login){
        String sql = "SELECT nome_cliente, id_cliente FROM tbl_login\n" +
                     "JOIN tbl_cliente ON tbl_cliente.FK_id_login = tbl_login.id_login\n" +
                     "WHERE usuario = ? AND senha = ? AND tbl_cliente.estatus = 'A';";
        ArrayList<String> dados = new ArrayList<>();
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            stmt.setString(2, login.getSenha());
            
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                dados.add(rs.getString(1));
                dados.add(rs.getString(2));
            }            
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        return dados;
    }
    
    public boolean verificarUsuario(Login login){
        boolean existe = false;
        String sql = "SELECT * FROM tbl_login WHERE usuario = ? AND estatus = 'A'";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, login.getUsuario());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                existe = true;
            }
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
        return existe;
    }
}
