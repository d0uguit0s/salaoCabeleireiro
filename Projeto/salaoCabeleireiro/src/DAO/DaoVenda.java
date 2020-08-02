/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Produto;
import Model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class DaoVenda {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    //ADICIONAR
    public double valorServico(String nomeServico){
        String sql = "SELECT preco_servico FROM tbl_servico WHERE nome_servico = ?;";
        double preco = 0;
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeServico);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                preco = Double.parseDouble(rs.getString(1));
            }
            return preco;
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    public ResultSet carregaProdutos(String codigoProduto){
        String sql = "SELECT * FROM tbl_produto WHERE id_produto = ? AND estatus = 'A'";
        ResultSet rs;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigoProduto);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }
    
    public void adicionar (Venda venda){
        String sql = "INSERT INTO tbl_venda (total_venda, FK_id_agendamento) VALUES(?, ?);";
        String sql2 = "UPDATE tbl_agendamento SET estatus = 'V' WHERE id_agendamento = ?";
        String sql3 = "SELECT id_venda FROM tbl_venda WHERE FK_id_agendamento = ?";
        ResultSet rs;
        int codigo = 0;
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDouble(1, venda.getTotalVendas());
            stmt.setString(2, venda.getCodigoAgendamento());
            stmt.execute();
            stmt.close();   
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, venda.getCodigoAgendamento());
            stmt2.execute();
            stmt2.close();  
            
            PreparedStatement stmt3 = connection.prepareStatement(sql3);
            stmt3.setString(1, venda.getCodigoAgendamento());
            rs = stmt3.executeQuery();
            
            while(rs.next()){
                codigo = rs.getInt(1);
            }
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        for (Produto produto : venda.getProdutos()) {
            vincularProdutos(codigo, produto);
        }
    }
    
    public void vincularProdutos(int codigo, Produto produto){
        String sql = "INSERT INTO venda_produto(FK_id_venda, FK_id_produto) "
                + "VALUES(?, "
                + "(SELECT id_produto FROM tbl_produto WHERE nome_produto = ?));";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigo);
            stmt.setString(2, produto.getNome());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    //CONSULTAR
    public ResultSet listar(){
        String sql = "SELECT id_venda, FK_id_agendamento, total_venda FROM tbl_venda WHERE estatus = 'A';";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarProdutos(String codigoVenda){
        String sql = "SELECT nome_produto FROM tbl_venda\n" +
                     "JOIN venda_produto ON venda_produto.FK_id_venda = tbl_venda.id_venda\n" +
                     "JOIN tbl_produto ON tbl_produto.id_produto = venda_produto.FK_id_produto\n" +
                     "WHERE id_venda = ?;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigoVenda);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    //ALTERAR
    public ArrayList<String> valorProduto(String nomeProduto){
        String sql = "SELECT id_produto, valor_produto FROM tbl_produto WHERE nome_produto = ?;";
        ArrayList<String> dados = new ArrayList<>();
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeProduto);
            
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                dados.add(rs.getString(1));
                dados.add(rs.getString(2));
            }
            return dados;
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
    }
    
    public void alterar(Venda venda, String codigo){
        //String sql = "UPDATE tbl_servico SET nome_servico = ?, preco_servico = ?, descricao_servico = ? WHERE id_servico = ?";
        
        String sql = "UPDATE tbl_venda SET total_venda = ?, FK_id_agendamento = ? WHERE id_venda = ?;";
        
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql);
            stmt1.setDouble(1, venda.getTotalVendas());
            stmt1.setString(2, venda.getCodigoAgendamento());
            stmt1.setString(3, codigo);
            stmt1.execute();
            stmt1.close();      
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        
        removerVinculoProdutos(codigo);
        
        for (Produto produto : venda.getProdutos()) {
            vincularProdutos(codigo, produto);
        }
    }
    
    public void removerVinculoProdutos(String codigo){
        String sql = "DELETE FROM venda_produto WHERE FK_id_venda = ?;";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void vincularProdutos(String codigo, Produto produto){
        String sql = "INSERT INTO venda_produto(FK_id_venda, FK_id_produto) "
                + "VALUES(?, "
                + "(SELECT id_produto FROM tbl_produto WHERE nome_produto = ?));";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.setString(2, produto.getNome());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_venda SET estatus = 'D' WHERE id_venda = ?;";
        
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

