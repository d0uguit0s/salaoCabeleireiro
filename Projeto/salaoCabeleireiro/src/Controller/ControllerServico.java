/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoServico;
import Model.Servico;
import View.Servico.Altera_Servico;
import View.Servico.Cad_Servico;
import View.Servico.Consulta_Servico;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Douglas
 */
public class ControllerServico{
    public final Cad_Servico telaCadastro;
    public final Consulta_Servico telaConsulta;
    public final Altera_Servico telaAltera;
    
    public ControllerServico(Cad_Servico telaCadastro, Consulta_Servico telaConsulta, Altera_Servico telaAltera){
        this.telaCadastro = telaCadastro;
        this.telaConsulta = telaConsulta;
        this.telaAltera = telaAltera;
        
    }
    
    //UTIL
    public Servico validaCampos(Cad_Servico telaCadastro, String nome, String preco, String descricao){
        if(nome.isEmpty() || preco.isEmpty() || descricao.isEmpty()){
                telaCadastro.tremeTela();
            if(nome.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldNome());
            }
            if(preco.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldPreco());
            }
            if(descricao.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextAreaDescricao());
            }
            return null;
        }else{
            Servico servico = new Servico(nome, Double.parseDouble(preco), descricao, null);
            return servico;
        }
    }
    
    public Servico validaCampos(Altera_Servico telaAltera, String nome, String preco, String descricao){
        if(nome.isEmpty() || preco.isEmpty() || descricao.isEmpty()){
            telaAltera.tremeTela();
            if(nome.isEmpty()){
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldNome());
            }
            if(preco.isEmpty()){
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldPreco());
            }
            if(descricao.isEmpty()){
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextAreaDescricao());
            }
            return null;
        }else{
            Servico servico = new Servico(nome, Double.parseDouble(preco), descricao, null);
            return servico;
        }
    }
    
    //CADASTRAR
    public void validaCamposCad(String nome, String preco, String descricao){
        Servico servico = validaCampos(telaCadastro, nome, preco, descricao);
        if(servico == null){
        }else{
            validaCadastro(servico);
        }
    }
    
    public void validaCadastro(Servico servico){
        DaoServico daoServico = new DaoServico();
        boolean servicoExiste = daoServico.verificarServico(servico);
        
        if(servicoExiste == false){
            daoServico.adicionar(servico);
            telaCadastro.caixaMensagem("Serviço cadastrado com sucesso!", "Serviço cadastrado", 1);
        }else{
            telaCadastro.caixaMensagem("O serviço que você tentou cadastrar, já existe!", "Serviço existente", 0);
        }
    }
    
    
    //LISTAR
    public void carregarLista(JTable tableServico){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoServico daoServico = new DaoServico();
        ResultSet rs = daoServico.listar();
        
        try{
            while(rs.next()){
                modelo.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    //ALTERAR
    public void resgatarRegistro(JTable tabela){
        String codigo, nome, preco, descricao;
        
        int linha = tabela.getSelectedRow();
        if(linha == -1){
            telaConsulta.caixaMensagem("Escolha um linha primeiro", "Nenhuma linha selecionada", 0);
        }else{
            codigo = (String) tabela.getValueAt(linha, 0);
            nome = (String) tabela.getValueAt(linha, 1);
            preco = (String) tabela.getValueAt(linha, 2);
            descricao = (String) tabela.getValueAt(linha, 3);
            carregarRegistro(nome, preco, descricao, codigo);
        }
    }
    
    public void carregarRegistro(String nome, String preco, String descricao, String codigo){
        Altera_Servico altera = new Altera_Servico(null, true);
        altera.preencheCampos(nome, preco, descricao, codigo);
        altera.setVisible(true);
    }
    
    public void validaCamposAlt(String nome, String preco, String descricao, String codigo){
        Servico servico = validaCampos(telaCadastro, nome, preco, descricao);
        if(servico == null){
        }else{
            validaAlteracao(servico, codigo);
        }
    }
    
    public void validaAlteracao(Servico servico, String codigo){
        DaoServico daoServico = new DaoServico();
        boolean servicoExiste = daoServico.verificarServico(servico);
        
        if(servicoExiste == false){
            daoServico.alterar(servico, codigo);
            telaAltera.caixaMensagem("Serviço atualizado com sucesso!", "Serviço atualizado", 1);
            telaAltera.dispose();
        }else{
            telaAltera.caixaMensagem("O nome do serviço que você tentou atualizar, já existe!", "Serviço existente", 0);
        }
    }
    
    //DESATIVAR    
    public void confirma(String codigo){
        int escolha = telaConsulta.caixaOpcao("Certeza que deseja excluir este servico?", "Confirmar exclusão");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoServico daoServico = new DaoServico();
        
        daoServico.desativar(codigo);
        telaConsulta.caixaMensagem("Servico excluido com sucesso!", "Servico excluido", 1);
    }
}
