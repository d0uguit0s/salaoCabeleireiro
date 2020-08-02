/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoProduto;
import Model.Produto;
import View.Produto.Altera_Produto;
import View.Produto.Cad_Produto;
import View.Produto.Consulta_Produto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Douglas
 */
public class ControllerProduto {
    public final Cad_Produto telaCadastro;
    public final Consulta_Produto telaConsulta;
    public final Altera_Produto telaAltera;
    
    public ControllerProduto(Cad_Produto telaCadastro, Consulta_Produto telaConsulta, Altera_Produto telaAltera){
        this.telaCadastro = telaCadastro;
        this.telaConsulta = telaConsulta;
        this.telaAltera = telaAltera;
        
    }
    
    //UTIL
    public Produto validaCampos(Cad_Produto telaCadastro, String nome, String preco, String validade){
        if(nome.isEmpty() || preco.isEmpty() || validade.isEmpty()){
                telaCadastro.tremeTela();
            if(nome.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldNome());
            }
            if(preco.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldPreco());
            }
            return null;
        }else{
            String[] dataEdita = validade.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Produto produto = new Produto(nome, Double.parseDouble(preco), dataBanco);
            return produto;
        }
    }
    
    public Produto validaCampos(Altera_Produto telaAltera, String nome, String preco, String validade){
        if(nome.isEmpty() || preco.isEmpty() || validade.isEmpty()){
            telaAltera.tremeTela();
            if(nome.isEmpty()){
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldNome());
            }
            if(preco.isEmpty()){
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldPreco());
            }
            return null;
        }else{
            String[] dataEdita = validade.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Produto produto = new Produto(nome, Double.parseDouble(preco), dataBanco);
            return produto;
        }
    }
    
    //CADASTRAR
    public void validaCamposCad(String nome, String preco, String validade){
        Produto produto = validaCampos(telaCadastro, nome, preco, validade);
        if(produto == null){
        }else{
            validaCadastro(produto);
        }
    }
    
    public void validaCadastro(Produto produto){
        DaoProduto daoServico = new DaoProduto();
        boolean servicoExiste = daoServico.verificarProduto(produto);
        
        if(servicoExiste == false){
            daoServico.adicionar(produto);
            telaCadastro.caixaMensagem("Produto cadastrado com sucesso!", "Produto cadastrado", 1);
        }else{
            telaCadastro.caixaMensagem("O produto que você tentou cadastrar, já existe!", "Produto existente", 0);
        }
    }
    
    
    //LISTAR
    public void carregarLista(JTable tableServico){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoProduto daoProduto = new DaoProduto();
        ResultSet rs = daoProduto.listar();
        
        try{
            while(rs.next()){
                String data = "";
                if(rs.getString(4) != null){
                    String[] dataBanco = rs.getString(4).split("-");
                    data = dataBanco[2] + '/' + dataBanco[1] + '/' + dataBanco[0];
                }
                
                modelo.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    data
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    //ALTERAR
    public ArrayList<String> resgatarRegistro(JTable tabela){
        //String codigo, nome, cpf, email, telefone, usuario, senha, servicos;
        //System.out.println("passou");
        ArrayList<String> dados = new ArrayList<>();
        int linha = tabela.getSelectedRow();
        if(linha == -1){
            telaConsulta.caixaMensagem("Escolha uma linha primeiro", "Nenhuma linha selecionada", 0);
        }else{
            dados.add((String) tabela.getValueAt(linha, 0));
            dados.add((String) tabela.getValueAt(linha, 1));
            dados.add((String) tabela.getValueAt(linha, 2));
            dados.add((String) tabela.getValueAt(linha, 3));
        }
        //System.out.println(dados);
        return dados;
    }
    
    public void carregarRegistro(ArrayList<String> dados){
        telaAltera.preencheCampos(dados.get(1), dados.get(2), dados.get(3), dados.get(0));
    }
    
    public void validaCamposAlt(String nome, String preco, String validade, String codigo){
        Produto produto = validaCampos(telaAltera, nome, preco, validade);
        if(produto == null){
        }else{
            validaAlteracao(produto, codigo);
        }
    }
    
    public void validaAlteracao(Produto produto, String codigo){
        DaoProduto daoProduto = new DaoProduto();
        daoProduto.alterar(produto, codigo);
        telaAltera.caixaMensagem("Serviço atualizado com sucesso!", "Serviço atualizado", 1);
        telaAltera.dispose();
    }
    
    //DESATIVAR    
    public void confirma(String codigo){
        int escolha = telaConsulta.caixaOpcao("Certeza que deseja excluir este produto?", "Confirmar exclusão");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoProduto daoProduto = new DaoProduto();
        
        daoProduto.desativar(codigo);
        telaConsulta.caixaMensagem("Produto excluido com sucesso!", "Produto excluido", 1);
    }
}
