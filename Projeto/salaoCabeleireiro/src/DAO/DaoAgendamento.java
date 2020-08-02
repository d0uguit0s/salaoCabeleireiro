/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Model.Agendamento;
import Model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gabri
 */
public class DaoAgendamento {
    private Connection connection = new ConnectionFactory.Connection().getConnection();
    
    //ADICIONAR    
    public String getNomeCliente(String codigo){
        String sql = "SELECT nome_cliente FROM tbl_cliente WHERE id_cliente = ?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            String nomeCliente = "";
            while(rs.next()){
                nomeCliente = rs.getString(1);
            }
            return nomeCliente;
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }
    
    public ResultSet carregaServicos(String nomeServico, String nomeFuncionario){
        //String sql = "SELECT nome_servico, preco_servico, descricao_servico FROM tbl_servico WHERE nome_servico = ?";
        
        String sql = "SELECT tbl_servico.nome_servico, tbl_servico.preco_servico, tbl_servico.descricao_servico, tbl_funcionario.nome_funcionario FROM tbl_servico\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE nome_servico = ? AND nome_funcionario = ? AND tbl_servico.estatus = 'A';";
        ResultSet rs;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeServico);
            stmt.setString(2, nomeFuncionario);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException erro) {
            throw new RuntimeException(erro);
        }
    }
    
    public void adicionar (Agendamento agendamento, String codigoCliente){
        String sql1 = "INSERT INTO tbl_agendamento (data_agendamento, horario_agendamento, FK_id_cliente) VALUES (?, ?, ?);";
        String sql2 = "SELECT id_agendamento FROM tbl_agendamento ORDER BY id_agendamento DESC LIMIT 1;";
        String codigo = "";
        
        ResultSet rs;
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, agendamento.getData());
            stmt1.setString(2, agendamento.getHora());
            stmt1.setString(3, codigoCliente);
            stmt1.execute();
            stmt1.close();
            
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            rs = stmt2.executeQuery();
            
            while(rs.next()){
                System.out.println(rs.getString(1));
                codigo = rs.getString(1);
            }
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        System.out.println(codigo);
        for (Servico servico : agendamento.getServicos()) {
            //System.out.println(servico.getNome());
            vincularServicos(codigo, servico);
        }
    }
    
    public void vincularServicos(String codigo, Servico servico){        
        String sql = "INSERT INTO servico_agendamento (FK_id_agendamento, FK_id_servico) VALUES (?, "
                + "(SELECT id_servico FROM tbl_servico JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario WHERE nome_servico = ? AND nome_funcionario = ?));";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(codigo));
            System.out.println(servico.getNome());
            System.out.println(servico.getNomeFuncionario());
            stmt.setString(2, servico.getNome());
            stmt.setString(3, servico.getNomeFuncionario());
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    //CONSULTAR
    public ResultSet listarAgendaCliente(String codigo){
        String sql = "SELECT tbl_agendamento.id_agendamento, tbl_agendamento.data_agendamento, tbl_agendamento.horario_agendamento, tbl_agendamento.FK_id_cliente FROM tbl_agendamento\n" +
                     "JOIN servico_agendamento ON servico_agendamento.FK_id_agendamento = tbl_agendamento.id_agendamento\n" +
                     "JOIN tbl_servico ON tbl_servico.id_servico = servico_agendamento.FK_id_servico\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE tbl_agendamento.FK_id_cliente = ? AND tbl_agendamento.estatus = 'A' AND tbl_funcionario.estatus = 'A'\n" +
                     "group by id_agendamento;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarServicosCliente(int codigoAgendamento){
        String sql = "SELECT tbl_servico.nome_servico, tbl_Funcionario.nome_funcionario FROM servico_agendamento\n" +
                     "JOIN tbl_servico ON tbl_servico.id_servico = servico_agendamento.FK_id_servico\n" +
                     "JOIN tbl_agendamento ON tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "JOIN funcionario_servico ON funcionario_servico.FK_id_servico = tbl_servico.id_servico\n" +
                     "JOIN tbl_funcionario ON tbl_funcionario.id_funcionario = funcionario_servico.FK_id_funcionario\n" +
                     "WHERE tbl_agendamento.id_agendamento = ?\n" +
                     "GROUP BY nome_servico;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigoAgendamento);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarAgendaFuncionario(String codigo){
        System.out.println(codigo);
        String sql = "select id_agendamento, nome_cliente, data_agendamento, horario_agendamento from tbl_funcionario\n" +
                     "join funcionario_servico on funcionario_servico.FK_id_funcionario = tbl_funcionario.id_funcionario\n" +
                     "join tbl_servico on tbl_servico.id_servico = funcionario_servico.FK_id_servico\n" +
                     "join servico_agendamento on servico_agendamento.FK_id_servico = tbl_servico.id_servico\n" +
                     "join tbl_agendamento on tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "join tbl_cliente on tbl_cliente.id_cliente = tbl_agendamento.FK_id_cliente\n" +
                     "where id_funcionario = ? and data_agendamento >= DATE(NOW()) and tbl_agendamento.estatus = 'A' and tbl_cliente.estatus = 'A'\n" +
                     "group by id_agendamento;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarServicosFuncionario(String codigoFuncionario, String codigoAgendamento){
        String sql = "select nome_servico from tbl_funcionario\n" +
                     "join funcionario_servico on funcionario_servico.FK_id_funcionario = tbl_funcionario.id_funcionario\n" +
                     "join tbl_servico on tbl_servico.id_servico = funcionario_servico.FK_id_servico\n" +
                     "join servico_agendamento on servico_agendamento.FK_id_servico = tbl_servico.id_servico\n" +
                     "join tbl_agendamento on tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "join tbl_cliente on tbl_cliente.id_cliente = tbl_agendamento.FK_id_cliente\n" +
                     "where id_funcionario = ? and data_agendamento >= DATE(NOW()) and id_agendamento = ?;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigoFuncionario);
            stmt.setString(2, codigoAgendamento);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarAgendaGeral(){
        /*String sql = "select id_agendamento, nome_cliente, data_agendamento, horario_agendamento from tbl_funcionario\n" +
                     "join funcionario_servico on funcionario_servico.FK_id_funcionario = tbl_funcionario.id_funcionario\n" +
                     "join tbl_servico on tbl_servico.id_servico = funcionario_servico.FK_id_servico\n" +
                     "join servico_agendamento on servico_agendamento.FK_id_servico = tbl_servico.id_servico\n" +
                     "join tbl_agendamento on tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "join tbl_cliente on tbl_cliente.id_cliente = tbl_agendamento.FK_id_cliente\n" +
                     "where data_agendamento >= DATE(NOW()) and tbl_agendamento.estatus = 'A' and tbl_cliente.estatus = 'A'\n" +
                     "group by id_agendamento;";*/
        String sql = "select id_agendamento, nome_cliente, data_agendamento, horario_agendamento from tbl_agendamento\n" +
"join tbl_cliente on tbl_cliente.id_cliente = tbl_agendamento.FK_id_cliente\n" +
"where data_agendamento >= DATE(NOW()) and tbl_agendamento.estatus = 'A';";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    public ResultSet listarServicosGeral(String codigoAgendamento){
        String sql = "select nome_servico from tbl_funcionario\n" +
                     "join funcionario_servico on funcionario_servico.FK_id_funcionario = tbl_funcionario.id_funcionario\n" +
                     "join tbl_servico on tbl_servico.id_servico = funcionario_servico.FK_id_servico\n" +
                     "join servico_agendamento on servico_agendamento.FK_id_servico = tbl_servico.id_servico\n" +
                     "join tbl_agendamento on tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "join tbl_cliente on tbl_cliente.id_cliente = tbl_agendamento.FK_id_cliente\n" +
                     "where data_agendamento >= DATE(NOW()) and id_agendamento = ?;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigoAgendamento);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
    
    /*public ResultSet listarServicosFuncionario(int codigoAgendamento){
        String sql = "SELECT tbl_servico.nome_servico FROM servico_agendamento\n" +
                     "JOIN tbl_servico ON tbl_servico.id_servico = servico_agendamento.FK_id_servico\n" +
                     "JOIN tbl_agendamento ON tbl_agendamento.id_agendamento = servico_agendamento.FK_id_agendamento\n" +
                     "WHERE tbl_agendamento.FK_id_cliente = ?;";
        ResultSet rs;
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, codigoAgendamento);
            rs = stmt.executeQuery();
            return rs;
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }*/
    
    //ATUALIZAR
    public void alterar(Agendamento agendamento, String codigo){
        //String sql = "UPDATE tbl_servico SET nome_servico = ?, preco_servico = ?, descricao_servico = ? WHERE id_servico = ?";
        
        String sql1 = "UPDATE tbl_agendamento SET data_agendamento = ?, horario_agendamento = ? WHERE id_agendamento = ?;";
        //String sql2 = "UPDATE tbl_funcionario SET nome_funcionario = ?, cpf_funcionario = ?, email_funcionario = ?, telefone_funcionario = ?, FK_id_login = (SELECT id_login FROM tbl_login WHERE usuario = ? AND senha = ?) WHERE id_funcionario = ?;";
        
        try{
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setString(1, agendamento.getData());
            stmt1.setString(2, agendamento.getHora());
            stmt1.setString(3, codigo);
            stmt1.execute();
            stmt1.close();
            
            /*PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setString(1, funcionario.getNome());
            stmt2.setString(2, funcionario.getCpf());
            stmt2.setString(3, funcionario.getEmail());
            stmt2.setString(4, funcionario.getTelefone());
            stmt2.setString(5, funcionario.getCredenciais().getUsuario());
            stmt2.setString(6, funcionario.getCredenciais().getSenha());
            stmt2.setString(7, codigo);
            stmt2.execute();
            stmt2.close();    */     
        }catch(SQLException erro){
            throw new RuntimeException(erro);
        }
        
        removerVinculoServicos(codigo);
        
        agendamento.getServicos().forEach((servico) ->{
            vincularServicos(codigo, servico);
        });
    }
    
    public void removerVinculoServicos(String codigo){
        String sql = "DELETE FROM servico_agendamento WHERE FK_id_agendamento = ?;";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, codigo);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /*public void vincularServicos(String codigo, Servico servico){
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
    }*/
    
    //DESATIVAR
    public void desativar(String codigo){
        String sql = "UPDATE tbl_agendamento SET estatus = 'D' WHERE id_agendamento = ?;";
        
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

    

