/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gabri
 */
public class DaoProduto {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    public boolean verificarProduto(Produto produto){
        boolean existe = false;
        String sql = "SELECT nome_produto FROM tbl_produto WHERE nome_produto = ? AND estatus = 'A'";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                existe = true;
            }
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
        return existe;
    }
    
    public void adicionar (Produto produto){
        String sql = "INSERT INTO tbl_produto(nome_produto, valor_produto, validade_produto) VALUES(?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getValor());
            stmt.setString(3, produto.getValidade());
            
            stmt.execute();
            stmt.close();
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    public ResultSet listar(){
        String sql = "SELECT * FROM tbl_produto WHERE estatus = 'A'";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public void alterar(Produto produto, String codigo){
        String sql = "UPDATE tbl_produto SET nome_produto = ?, valor_produto = ?, validade_produto = ? WHERE id_produto = ?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getValor());
            stmt.setString(3, produto.getValidade());
            stmt.setString(4, codigo);
            
            stmt.execute();
            stmt.close();
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_produto SET estatus = 'D' WHERE id_produto = ?;";
        
         try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

    

