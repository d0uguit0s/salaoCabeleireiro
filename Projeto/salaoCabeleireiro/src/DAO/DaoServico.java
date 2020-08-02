/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public class DaoServico {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    public boolean verificarServico(Servico servico){
        boolean existe = false;
        String sql = "SELECT nome_servico FROM tbl_servico WHERE nome_servico = ? AND estatus = 'A'";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getNome());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                existe = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoFuncionario.class.getName()).log(Level.ALL.SEVERE, null, ex);
        }
        return existe;
    }
    
    public void adicionar (Servico servico){
        String sql = "INSERT INTO tbl_servico(nome_servico, preco_servico, descricao_servico) VALUES(?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setString(3, servico.getDescricao());
            
            stmt.execute();
            stmt.close();
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    public ResultSet listar(){
        //String sql = "SELECT id_servico, nome_servico, preco_servico, descricao_servico FROM tbl_servico";
        String sql = "SELECT tbl_servico.id_servico, tbl_servico.nome_servico, tbl_servico.preco_servico, tbl_servico.descricao_servico, tbl_funcionario.nome_funcionario FROM tbl_servico\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE tbl_servico.estatus = 'A'\n" +
                     "GROUP BY tbl_servico.nome_servico\n" +
                     "ORDER BY tbl_servico.id_servico;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarServicosDisponiveis(){
        //String sql = "SELECT id_servico, nome_servico, preco_servico, descricao_servico FROM tbl_servico";
        String sql = "SELECT tbl_servico.id_servico, tbl_servico.nome_servico, tbl_servico.preco_servico, tbl_servico.descricao_servico, tbl_funcionario.nome_funcionario FROM tbl_servico\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE tbl_servico.estatus = 'A' AND tbl_funcionario.estatus = 'A'\n" +
                     "ORDER BY tbl_servico.id_servico;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public void alterar(Servico servico, String codigo){
        String sql = "UPDATE tbl_servico SET nome_servico = ?, preco_servico = ?, descricao_servico = ? WHERE id_servico = ?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setString(3, servico.getDescricao());
            stmt.setString(4, codigo);
            
            stmt.execute();
            stmt.close();
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_servico SET estatus = 'D' WHERE id_servico = ?;";
        
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

