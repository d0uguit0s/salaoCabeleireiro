/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoProduto;
import DAO.DaoServico;
import DAO.DaoVenda;
import Model.Produto;
import Model.Venda;
import View.Agendamento.Consulta_Agendamento_Geral;
import View.Venda.Altera_Venda;
import View.Venda.Consulta_Venda;
import View.Venda.Reg_Venda;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Douglas
 */
public class ControllerVenda {
    public final Reg_Venda telaRegistro;
    public final Consulta_Venda telaConsulta;
    public final Altera_Venda telaAltera;
    public ArrayList<ArrayList<String>> produtosDisp = new ArrayList<>();
    public ArrayList<ArrayList<String>> produtosAdd = new ArrayList<>();
    double total = 0;
    
    public ControllerVenda(Reg_Venda telaRegistro, Consulta_Venda telaConsulta, Altera_Venda telaAltera){
        this.telaRegistro = telaRegistro;
        this.telaConsulta = telaConsulta;
        this.telaAltera = telaAltera;
        
    }

    public double getTotal() {
        return total;
    }
    
    
    
    //CADASTRAR
    public void carregaVenda(JTable tabela){
        ArrayList<String> dados = new ArrayList<>();
        int linha = tabela.getSelectedRow();
        if(linha == -1){
            telaRegistro.caixaMensagem("Escolha uma linha primeiro", "Nenhuma linha selecionada", 0);
            telaRegistro.dispose();
        }else{
            dados.add((String) tabela.getValueAt(linha, 0));
            dados.add((String) tabela.getValueAt(linha, 4));
        }
        String[] nomeServicos = dados.get(1).split(", ");
        for (String nomeServico : nomeServicos) {
            DaoVenda daoVenda = new DaoVenda();
            total += daoVenda.valorServico(nomeServico);
        }
        telaRegistro.preencheCampos(String.valueOf(total), dados.get(0));
    }
    
    public void carregarListaProdutos(JTable tabelaProdutosDisp){
        DefaultTableModel modelo = (DefaultTableModel) tabelaProdutosDisp.getModel();
        modelo.setNumRows(0);
        
        DaoProduto daoProduto = new DaoProduto();
        ResultSet rs = daoProduto.listar();
        
        try{
            while(rs.next()){
                modelo.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
                });
                ArrayList<String> produtos = new ArrayList<>();
                produtos.add(rs.getString(1));
                produtos.add(rs.getString(2));
                produtos.add(rs.getString(3));
                
                produtosDisp.add(produtos);
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
        //System.out.println(servicosDisp);
    }
    
    public void adicionaProdutos(JTable tabelaProdutosDisp, JTable tabelaProdutosAdd){
        ArrayList<String> produtos = new ArrayList<>();
        int linha = tabelaProdutosDisp.getSelectedRow();
        if(linha > -1){
            produtos.add((String) tabelaProdutosDisp.getValueAt(linha, 0));
            produtos.add((String) tabelaProdutosDisp.getValueAt(linha, 1));
            produtos.add((String) tabelaProdutosDisp.getValueAt(linha, 2));
        
            //System.out.println(servico);

            produtosAdd.add(produtos);
            //produtosDisp.remove(produtos);

            //System.out.println(servicosAdd);
            //System.out.println(servicosDisp);

            DefaultTableModel modelo = (DefaultTableModel) tabelaProdutosAdd.getModel();
            modelo.setNumRows(0);

            for(int i = 0; i < produtosAdd.size(); i++){
                modelo.addRow(new Object[]{
                    produtosAdd.get(i).get(0),
                    produtosAdd.get(i).get(1),
                    produtosAdd.get(i).get(2)
                });
            }

            //DefaultTableModel modelo2 = (DefaultTableModel) tabelaProdutosDisp.getModel();
            //modelo2.removeRow(tabelaProdutosDisp.getSelectedRow());
            
            double valorUnit = Double.parseDouble((String)tabelaProdutosDisp.getValueAt(linha, 2));
            total += valorUnit;
        }else{
            telaRegistro.caixaMensagem("Selecione um produto para adicionar!", "Nenhuma linha selecionada", 0);
        }
        
    }
    
    public void removeProdutos(JTable tabelaProdutosDisp, JTable tabelaProdutosAdd){
        ArrayList<String> produtos = new ArrayList<>();
        int linha = tabelaProdutosAdd.getSelectedRow();
        //System.out.println(linha);
        if(linha > -1){
            produtos.add((String) tabelaProdutosAdd.getValueAt(linha, 0));
            produtos.add((String) tabelaProdutosAdd.getValueAt(linha, 1));
            produtos.add((String) tabelaProdutosAdd.getValueAt(linha, 2));

            //produtosDisp.add(produtos);
            produtosAdd.remove(produtos);
            System.out.println(produtos);

            //DefaultTableModel modelo = (DefaultTableModel) tabelaProdutosDisp.getModel();
            //modelo.setNumRows(0);

            /*for(int i = 0; i < produtosDisp.size(); i++){
                modelo.addRow(new Object[]{
                    produtosAdd.get(i).get(0),
                    produtosAdd.get(i).get(1),
                    produtosAdd.get(i).get(2)
                });
            }*/

            DefaultTableModel modelo2 = (DefaultTableModel) tabelaProdutosAdd.getModel();
            modelo2.removeRow(tabelaProdutosAdd.getSelectedRow());
            
            double valorUnit = Double.parseDouble((String)tabelaProdutosDisp.getValueAt(linha, 2));
            total -= valorUnit;
        }else{
            telaRegistro.caixaMensagem("Selecione um produto para remover!", "Nenhuma linha selecionada", 0);
        }        
    }
    
    public void validaRegistro(double total, String codigoAgendamento, Consulta_Agendamento_Geral tela){
        DaoVenda daoVenda = new DaoVenda();
        ArrayList<Produto> produtos = new ArrayList<>();
        produtosAdd.forEach((produto) -> {
            ResultSet rs = daoVenda.carregaProdutos(produto.get(0));
            try {
                while(rs.next()){
                    Produto prod = new Produto(rs.getString(2), Double.parseDouble(rs.getString(3)), rs.getString(4));
                    produtos.add(prod);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        Venda venda = new Venda(total, codigoAgendamento, produtos);
        daoVenda.adicionar(venda);
        telaRegistro.caixaMensagem("Venda registrada com sucesso!", "Venda registrada", 1);
        tela.dispose();
        telaRegistro.dispose();
    }
    
    //LISTAR
    public void carregarLista(JTable tableServico){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoVenda daoVenda = new DaoVenda();
        ResultSet rs1 = daoVenda.listar();
        
        try{
            while(rs1.next()){
                
                String registroProdutos = "";
                ResultSet rs2 = daoVenda.listarProdutos(rs1.getString(1));
                while(rs2.next()){
                    if(rs2.isLast()){
                        registroProdutos += rs2.getString(1);
                    }else{
                        registroProdutos += rs2.getString(1) + ", ";
                    }
                }
                //System.out.println(registroServico);                
                modelo.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    registroProdutos,
                    rs1.getString(3)
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    //ALTERAR
    public String ResgatarVenda(JTable tabela, JTable tabelaDisp, JTable tabelaAdd){
        ArrayList<String> dados = new ArrayList<>();
        String codigo = "";
        int linha = tabela.getSelectedRow();
        if(linha == -1){
            telaRegistro.caixaMensagem("Escolha uma linha primeiro", "Nenhuma linha selecionada", 0);
            telaRegistro.dispose();
        }else{
            dados.add((String) tabela.getValueAt(linha, 1));
            dados.add((String) tabela.getValueAt(linha, 3));
            dados.add((String) tabela.getValueAt(linha, 2));
            codigo = (String) tabela.getValueAt(linha, 0);
        }
        if(!dados.get(2).isEmpty()){
            String[] nomeProdutos = dados.get(2).split(", ");
            for (String nomeProduto : nomeProdutos) {
                DaoVenda daoVenda = new DaoVenda();
                ArrayList<String> dadosProduto = daoVenda.valorProduto(nomeProduto);
                ArrayList<String> produto = new ArrayList<>();
                produto.add(dadosProduto.get(0));
                produto.add(nomeProduto);
                produto.add(dadosProduto.get(1));

                produtosAdd.add(produto);
            }
            organizarTabelas(tabelaDisp, tabelaAdd);
        }
        
        total = Double.parseDouble(dados.get(1));
        telaAltera.preencheCampos(dados.get(1), dados.get(0));
        
        return codigo;
    }
    
    public void organizarTabelas(JTable tabelaDisp, JTable tabelaAdd){
        
        DefaultTableModel modelo = (DefaultTableModel) tabelaAdd.getModel();
        modelo.setNumRows(0);

        for(int i = 0; i < produtosAdd.size(); i++){
            modelo.addRow(new Object[]{
                produtosAdd.get(i).get(0),
                produtosAdd.get(i).get(1),
                produtosAdd.get(i).get(2)
            });
        }
    }
    
    public void validaAlteracao(double total, String codigoAgendamento, String codigoVenda){
        DaoVenda daoVenda = new DaoVenda();
        ArrayList<Produto> produtos = new ArrayList<>();
        produtosAdd.forEach((produto) -> {
            ResultSet rs = daoVenda.carregaProdutos(produto.get(0));
            try {
                while(rs.next()){
                    Produto prod = new Produto(rs.getString(2), Double.parseDouble(rs.getString(3)), rs.getString(4));
                    produtos.add(prod);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        
        Venda venda = new Venda(total, codigoAgendamento, produtos);
        
        daoVenda.alterar(venda, codigoVenda);
        telaAltera.caixaMensagem("Venda atualizada com sucesso!", "Venda atualizada", 1);
    
    }
    
    //DESATIVAR    
    public void confirma(String codigo){
        int escolha = telaConsulta.caixaOpcao("Certeza que deseja excluir este registro de venda?", "Confirmar exclusão");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoVenda daoVenda = new DaoVenda();
        
        daoVenda.desativar(codigo);
        telaConsulta.caixaMensagem("Venda excluida com sucesso!", "Venda excluida", 1);
    }
}
