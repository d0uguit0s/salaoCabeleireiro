/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoCliente;
import DAO.DaoLogin;
import Model.Cliente;
import Model.Login;
import View.Cliente.Altera_Cliente;
import View.Cliente.Cad_Cliente;
import View.Cliente.Consulta_Cliente;
import View.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Douglas
 */
public class ControllerCliente {
    public final Cad_Cliente telaCadastro;
    public final Consulta_Cliente telaConsulta;
    public final Altera_Cliente telaAltera;
    
    public ControllerCliente(Cad_Cliente telaCadastro, Consulta_Cliente telaConsulta, Altera_Cliente telaAltera){
        this.telaCadastro = telaCadastro;
        this.telaConsulta = telaConsulta;
        this.telaAltera = telaAltera;
        
    }
    
    //UTIL
    public Cliente validaCampos(Cad_Cliente telaCadastro, String nome, String cpf, String nasc, String email, String telefone, String usuario, String senha, String confirmaSenha){
        if(nome.isEmpty() || cpf.isEmpty() || nasc.isEmpty() || telefone.isEmpty()
           || usuario.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()){
            telaCadastro.tremeTela();
            if(nome.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldNome());
            }
            if(cpf.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldCpf());
            }
            if(nasc.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldNasc());
            }
            if(telefone.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldTelefone());
            }
            if(usuario.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldUsuario());
            }
            if(senha.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjPasswordFieldSenha());
            }
            if(confirmaSenha.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjPasswordFieldConfirmaSenha());
            }
            return null;
        }else if(!senha.equals(confirmaSenha)){
            telaCadastro.tremeTela();
            telaCadastro.caixaMensagem("As senhas inseridas são diferentes!", "Senhas incompatíveis", 0);
            return null;
        }else{       
            String[] dataEdita = nasc.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Login credenciais = new Login(usuario, senha);
            Cliente cliente = new Cliente(nome, cpf, dataBanco, email, telefone, credenciais);
            return cliente;
        }
    }
    
    public Cliente validaCampos(Altera_Cliente telaAltera, String nome, String cpf, String nasc, String email, String telefone, String usuario, String senha, String confirmaSenha){
        if(nome.isEmpty() || cpf.isEmpty() || nasc.isEmpty() || telefone.isEmpty()
           || usuario.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()){
            if(nome.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldNome());
            }
            if(cpf.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjFormattedTextFieldCpf());
            }
            if(nasc.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjFormattedTextFieldNasc());
            }
            if(telefone.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldTelefone());
            }
            if(usuario.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjTextFieldUsuario());
            }
            if(senha.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjPasswordFieldSenha());
            }
            if(confirmaSenha.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjPasswordFieldConfirmaSenha());
            }
            return null;
        }else{            
            String[] dataEdita = nasc.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Login credenciais = new Login(usuario, senha);
            Cliente cliente = new Cliente(nome, cpf, dataBanco, email, telefone, credenciais);
            return cliente;
        }
    }
    
    //CADASTRAR
    public void validaCamposCad(String nome, String cpf, String nasc, String email, String telefone, String usuario, String senha, String confirmaSenha){
        Cliente cliente = validaCampos(telaCadastro, nome, cpf, nasc, email, telefone, usuario, senha, confirmaSenha);
        if(cliente == null){
        }else{
            validaCadastro(cliente);
        }
    }
    
    public void validaCadastro(Cliente cliente){
        DaoCliente daoCliente = new DaoCliente();
        DaoLogin daoLogin = new DaoLogin();
        boolean existe = daoLogin.verificarUsuario(cliente.getCredenciais());
        
        if(existe == false){
            daoCliente.adicionar(cliente);
            telaCadastro.caixaMensagem("Cliente cadastrado com sucesso!", "Cliente cadastrado", 1);
            telaCadastro.dispose();
            new Main().setVisible(true);
        }else{
            telaCadastro.caixaMensagem("O usuário que você tentou cadastrar já existe, tente outro.", "Usuario existente", 0);
        }
    }
    
    
    //LISTAR
    public void carregarLista(JTable tableCliente){
        DefaultTableModel modelo = (DefaultTableModel) tableCliente.getModel();
        modelo.setNumRows(0);
        
        DaoCliente daoServico = new DaoCliente();
        ResultSet rs = daoServico.listar();
        
        try{
            while(rs.next()){
                String[] dataBanco = rs.getString(4).split("-");
                String data = dataBanco[2] + '/' + dataBanco[1] + '/' + dataBanco[0];
                modelo.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    data,
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8)
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    //ALTERAR
    public ArrayList<String> resgatarRegistro(JTable tabela){
        ArrayList<String> dados = new ArrayList<>();
        int linha = tabela.getSelectedRow();
        if(linha == -1){
            telaConsulta.caixaMensagem("Escolha uma linha primeiro", "Nenhuma linha selecionada", 0);
        }else{
            dados.add((String) tabela.getValueAt(linha, 0));
            dados.add((String) tabela.getValueAt(linha, 1));
            dados.add((String) tabela.getValueAt(linha, 2));
            dados.add((String) tabela.getValueAt(linha, 3));
            dados.add((String) tabela.getValueAt(linha, 4));
            dados.add((String) tabela.getValueAt(linha, 5));
            dados.add((String) tabela.getValueAt(linha, 6));
            dados.add((String) tabela.getValueAt(linha, 7));
            return dados;
        }
        //System.out.println(dados);
        return null;
    }
    
    public void carregarRegistro(ArrayList<String> dados){
        telaAltera.preencheCampos(dados.get(0), dados.get(1), dados.get(2), dados.get(3), dados.get(4), dados.get(5), dados.get(6), dados.get(7));
    }
    
    public void validaCamposAlt(String nome, String cpf, String nasc, String email, String telefone, String usuario, String senha, String confirmaSenha, String codigo){
        Cliente cliente = validaCampos(telaCadastro, nome, cpf, nasc, email, telefone, usuario, senha, confirmaSenha);
        if(cliente == null){
        }else{
            validaAlteracao(cliente, codigo);
        }
    }
    
    public void validaAlteracao(Cliente cliente, String codigo){
        DaoCliente daoCliente = new DaoCliente();
        
        daoCliente.alterar(cliente, codigo);
        telaAltera.caixaMensagem("Cliente atualizado com sucesso!", "Cliente atualizado", 1);
    }
    
    //DESATIVAR    
    public void confirma(String codigo){
        int escolha = telaConsulta.caixaOpcao("Certeza que deseja excluir este cliente?", "Confirmar exclusão");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoCliente daoAgendamento = new DaoCliente();
        
        daoAgendamento.desativar(codigo);
        telaConsulta.caixaMensagem("Cliente excluido com sucesso!", "Cliente excluido", 1);
    }
}
