/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Funcionario;
import Model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gabri
 */
public class DaoFuncionario {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    //ADICIONAR
    /*public ResultSet listarComboServicos(){
        String sql = "SELECT nome_servico FROM tbl_servico";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }*/
    
    public ResultSet carregaServicos(String nome){
        String sql = "SELECT nome_servico, preco_servico, descricao_servico FROM tbl_servico WHERE nome_servico = ? AND estatus = 'A'";
        ResultSet rs;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nome);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }
    
    public void adicionar (Funcionario funcionario){
        String sql1 = "INSERT INTO tbl_login(usuario, senha) VALUES (?, ?);";
        String sql2 = "INSERT INTO tbl_funcionario(nome_funcionario, cpf_funcionario, email_funcionario, telefone_funcionario, FK_id_login) VALUES (?, ?, ?, ?, (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ?));";
        String sql3 = "SELECT id_funcionario FROM tbl_funcionario WHERE cpf_funcionario = ?";
        int codigo = 0;
        
        ResultSet rs;
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, funcionario.getCredenciais().getUsuario());
            stmt1.setString(2, funcionario.getCredenciais().getSenha());
            stmt1.execute();
            stmt1.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, funcionario.getNome());
            stmt2.setString(2, funcionario.getCpf());
            stmt2.setString(3, funcionario.getEmail());
            stmt2.setString(4, funcionario.getTelefone());
            stmt2.setString(5, funcionario.getCredenciais().getUsuario());
            stmt2.setString(6, funcionario.getCredenciais().getSenha());
            stmt2.execute();
            stmt2.close();
            
            PreparedStatement stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, funcionario.getCpf());
            rs = stmt3.executeQuery();
            while(rs.next()){
                codigo = rs.getInt(1);
            }
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        for (Servico servico : funcionario.getServicos()) {
            System.out.println(servico.getNome());
            vincularServicos(codigo, servico);
        }
    }
    
    public void vincularServicos(int codigo, Servico servico){
        String sql = "INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) "
                + "VALUES(?, "
                + "(SELECT id_servico FROM tbl_servico WHERE nome_servico = ?));";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigo);
            stmt.setString(2, servico.getNome());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public ResultSet listarTabelaServicos(String servico){
        String sql = "SELECT nome_servico, preco_servico, descricao_servico FROM tbl_servico WHERE nome_servico = ? AND estatus = 'A'";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //CONSULTAR
    public ResultSet listar(){
        String sql = "SELECT tbl_funcionario.id_funcionario, tbl_funcionario.nome_funcionario, tbl_funcionario.cpf_funcionario, tbl_funcionario.email_funcionario, tbl_funcionario.telefone_funcionario, tbl_login.usuario, tbl_login.senha FROM tbl_funcionario\n" +
                     "JOIN tbl_login ON tbl_login.id_login = tbl_funcionario.FK_id_login\n" +
                     "WHERE tbl_funcionario.estatus = 'A';";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarServicos(String codigoFuncionario){
        String sql = "SELECT tbl_servico.nome_servico FROM tbl_servico\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE tbl_funcionario.id_funcionario = ? AND tbl_funcionario.estatus = 'A';";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigoFuncionario);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //ATUALIZAR
    public void alterar(Funcionario funcionario, String codigo){
        //String sql = "UPDATE tbl_servico SET nome_servico = ?, preco_servico = ?, descricao_servico = ? WHERE id_servico = ?";
        
        String sql1 = "UPDATE tbl_login SET usuario = ?, senha = ? WHERE id_login = (SELECT FK_id_login FROM tbl_funcionario WHERE id_funcionario = ?);";
        String sql2 = "UPDATE tbl_funcionario SET nome_funcionario = ?, cpf_funcionario = ?, email_funcionario = ?, telefone_funcionario = ?, FK_id_login = (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ?) WHERE id_funcionario = ?;";
        
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, funcionario.getCredenciais().getUsuario());
            stmt1.setString(2, funcionario.getCredenciais().getSenha());
            stmt1.setString(3, codigo);
            stmt1.execute();
            stmt1.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, funcionario.getNome());
            stmt2.setString(2, funcionario.getCpf());
            stmt2.setString(3, funcionario.getEmail());
            stmt2.setString(4, funcionario.getTelefone());
            stmt2.setString(5, funcionario.getCredenciais().getUsuario());
            stmt2.setString(6, funcionario.getCredenciais().getSenha());
            stmt2.setString(7, codigo);
            stmt2.execute();
            stmt2.close();         
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        
        removerVinculoServicos(codigo);
        
        funcionario.getServicos().forEach((servico) ->{
            vincularServicos(codigo, servico);
        });
    }
    
    public void removerVinculoServicos(String codigo){
        String sql = "DELETE FROM funcionario_servico WHERE FK_id_funcionario = ?;";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void vincularServicos(String codigo, Servico servico){
        String sql = "INSERT INTO funcionario_servico(FK_id_funcionario, FK_id_servico) "
                + "VALUES(?, "
                + "(SELECT id_servico FROM tbl_servico WHERE nome_servico = ?));";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.setString(2, servico.getNome());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_funcionario SET estatus = 'D' WHERE id_funcionario = ?;";
        String sql2 = "UPDATE tbl_login SET estatus = 'D'\n"+ 
                      "WHERE id_login = (SELECT FK_id_login FROM tbl_funcionario WHERE id_funcionario = ?);";
        
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
