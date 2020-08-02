/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoFuncionario;
import DAO.DaoLogin;
import DAO.DaoServico;
import Model.Funcionario;
import Model.Login;
import Model.Servico;
import View.Funcionario.Altera_Funcionario;
import View.Funcionario.Cad_Funcionario;
import View.Funcionario.Consulta_Funcionario;
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
public class ControllerFuncionario {
    public final Cad_Funcionario telaCadastro;
    public final Consulta_Funcionario telaConsulta;
    public final Altera_Funcionario telaAltera;
    public ArrayList<String> servicosDisp = new ArrayList<>();
    public ArrayList<String> servicosAdd = new ArrayList<>();
    
    public ControllerFuncionario(Cad_Funcionario telaCadastro, Consulta_Funcionario telaConsulta, Altera_Funcionario telaAltera){
        this.telaCadastro = telaCadastro;
        this.telaConsulta = telaConsulta;
        this.telaAltera = telaAltera;
    }
    
    //UTIL
    public Funcionario validaCampos(Cad_Funcionario telaCadastro, String nome, String cpf, String email, String telefone, String usuario, String senha, String confirmaSenha, int linhasServico){
        if(nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || usuario.isEmpty()
           || senha.isEmpty() || confirmaSenha.isEmpty() || linhasServico < 1){
            telaCadastro.tremeTela();
            if(nome.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldNome());
            }
            if(cpf.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldCpf());
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
            if(linhasServico < 1){
                telaCadastro.caixaMensagem("Escolha ao menos um serviço!", "Escolher serviço", 0);
            }
            return null;
        }else if(!senha.equals(confirmaSenha)){
            telaCadastro.tremeTela();
            telaCadastro.caixaMensagem("As senhas inseridas são diferentes!", "Senhas incompatíveis", 0);
            return null;
        }else{
            DaoFuncionario daoFuncionario = new DaoFuncionario();
            ArrayList<Servico> servicos = new ArrayList<>();
            servicosAdd.forEach((nomeServico) -> {
                ResultSet rs = daoFuncionario.carregaServicos(nomeServico);
                try {
                    while(rs.next()){
                        Servico servico = new Servico(rs.getString(1), rs.getDouble(2), rs.getString(3), null);
                        servicos.add(servico);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            
            Login credenciais = new Login(usuario, senha);
            Funcionario funcionario = new Funcionario(nome, cpf, email, telefone, credenciais, servicos);
            return funcionario;
        }
    }
    
    public Funcionario validaCampos(Altera_Funcionario telaAltera, String nome, String cpf, String email, String telefone, String usuario, String senha, String confirmaSenha, int linhasServico){
        if(nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || usuario.isEmpty()
           || senha.isEmpty() || confirmaSenha.isEmpty()){
            telaCadastro.tremeTela();
            if(nome.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjTextFieldNome());
            }
            if(cpf.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldCpf());
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
            if(linhasServico < 1){
                telaCadastro.caixaMensagem("Escolha ao menos um serviço!", "Escolher serviço", 0);
            }
            return null;
        }else if(!senha.equals(confirmaSenha)){
            telaCadastro.tremeTela();
            telaCadastro.caixaMensagem("As senhas inseridas são diferentes!", "Senhas incompatíveis", 0);
            return null;
        }else{
            DaoFuncionario daoFuncionario = new DaoFuncionario();
            ArrayList<Servico> servicos = new ArrayList<>();
            servicosAdd.forEach((nomeServico) -> {
            ResultSet rs = daoFuncionario.carregaServicos(nomeServico);
            try {
                while(rs.next()){
                    Servico servico = new Servico(rs.getString(1), rs.getDouble(2), rs.getString(3), null);
                    servicos.add(servico);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            });
            
            Login credenciais = new Login(usuario, senha);
            Funcionario funcionario = new Funcionario(nome, cpf, email, telefone, credenciais, servicos);
            return funcionario;
        }
    }
    
    //CADASTRAR
    public void carregarListaServicos(JTable tabelaServicosDisp){
        DefaultTableModel modelo = (DefaultTableModel) tabelaServicosDisp.getModel();
        modelo.setNumRows(0);
        
        DaoServico daoServico = new DaoServico();
        ResultSet rs = daoServico.listar();
        
        try{
            while(rs.next()){
                modelo.addRow(new Object[]{
                    rs.getString(2)
                });
                servicosDisp.add(rs.getString(2));
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
        //System.out.println(servicosDisp);
    }
    
    public void adicionaServicos(JTable tabelaServicosDisp, JTable tabelaServicosAdd){
        String servico;
        int linha = tabelaServicosDisp.getSelectedRow();
        if(linha > -1){
            servico = (String) tabelaServicosDisp.getValueAt(linha, 0);
        
            //System.out.println(servico);

            servicosAdd.add(servico);
            servicosDisp.remove(servico);

            //System.out.println(servicosAdd);
            //System.out.println(servicosDisp);

            DefaultTableModel modelo = (DefaultTableModel) tabelaServicosAdd.getModel();
            modelo.setNumRows(0);

            for(int i = 0; i < servicosAdd.size(); i++){
                modelo.addRow(new Object[]{
                    servicosAdd.get(i)
                });
            }

            DefaultTableModel modelo2 = (DefaultTableModel) tabelaServicosDisp.getModel();
            modelo2.removeRow(tabelaServicosDisp.getSelectedRow());
        }else{
            telaCadastro.caixaMensagem("Selecione um serviço para adicionar!", "Nenhuma linha selecionada", 0);
        }
        
    }
    
    public void removeServicos(JTable tabelaServicosDisp, JTable tabelaServicosAdd){
        String servico;
        int linha = tabelaServicosAdd.getSelectedRow();
        //System.out.println(linha);
        if(linha > -1){
            servico = (String) tabelaServicosAdd.getValueAt(linha, 0);

            servicosDisp.add(servico);
            servicosAdd.remove(servico);

            DefaultTableModel modelo = (DefaultTableModel) tabelaServicosDisp.getModel();
            modelo.setNumRows(0);

            for(int i = 0; i < servicosDisp.size(); i++){
                modelo.addRow(new Object[]{
                    servicosDisp.get(i)
                });
            }

            DefaultTableModel modelo2 = (DefaultTableModel) tabelaServicosAdd.getModel();
            modelo2.removeRow(tabelaServicosAdd.getSelectedRow());
        }else{
            telaCadastro.caixaMensagem("Selecione um serviço para remover!", "Nenhuma linha selecionada", 0);
        }        
    }
    
    public void validaCamposCad(String nome, String cpf, String email, String telefone, String usuario, String senha, String confirmaSenha, int linhasServico){
        Funcionario funcionario = validaCampos(telaCadastro, nome, cpf, email, telefone, usuario, senha, confirmaSenha, linhasServico);
        if(funcionario == null){
        }else{
            validaCadastro(funcionario);
        }
    }
    
    public void validaCadastro(Funcionario funcionario){
        DaoFuncionario daoFuncionario = new DaoFuncionario();
        DaoLogin daoLogin = new DaoLogin();
        boolean existe = daoLogin.verificarUsuario(funcionario.getCredenciais());
        
        if(existe == false){
            daoFuncionario.adicionar(funcionario);
            telaCadastro.caixaMensagem("Funcionário cadastrado com sucesso!", "Funcionário cadastrado", 1);
            telaCadastro.dispose();
            new Main().setVisible(true);
        }else{
            telaCadastro.caixaMensagem("O usuário que você tentou cadastrar já existe, tente outro.", "Usuario existente", 0);
        }   
    }
    
    
    //LISTAR
    public void carregarLista(JTable tableServico){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoFuncionario daoFuncionario = new DaoFuncionario();
        ResultSet rs1 = daoFuncionario.listar();
        
        try{
            while(rs1.next()){
                
                String registroServico = "";
                ResultSet rs2 = daoFuncionario.listarServicos(rs1.getString(1));
                while(rs2.next()){
                    if(rs2.isLast()){
                        registroServico += rs2.getString(1);
                    }else{
                        registroServico += rs2.getString(1) + ", ";
                    }
                }
                //System.out.println(registroServico);                
                modelo.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    rs1.getString(3),
                    rs1.getString(4),
                    rs1.getString(5),
                    rs1.getString(6),
                    rs1.getString(7),
                    registroServico
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
            dados.add((String) tabela.getValueAt(linha, 4));
            dados.add((String) tabela.getValueAt(linha, 5));
            dados.add((String) tabela.getValueAt(linha, 6));
            dados.add((String) tabela.getValueAt(linha, 7));
        }
        //System.out.println(dados);
        return dados;
    }
    
    public void carregarRegistro(ArrayList<String> dados, JTable tabelaDisp, JTable tabelaAdd){
        telaAltera.preencheCampos(dados.get(0), dados.get(1), dados.get(2), dados.get(3), dados.get(4), dados.get(5), dados.get(6));

        String servicos = dados.get(7);
        //System.out.println(servicos);
        String[] adicionados = servicos.split(", ");
        for (String adicionado : adicionados) {
            servicosAdd.add(adicionado);
            servicosDisp.remove(adicionado);
        }
        organizarTabelas(tabelaDisp, tabelaAdd);
        //System.out.println(servicosDisp);
        //System.out.println(servicosAdd);
    }
    
    public void organizarTabelas(JTable tabelaDisp, JTable tabelaAdd){
        System.out.println(servicosAdd);
        System.out.println(servicosDisp);
        
        DefaultTableModel modelo1 = (DefaultTableModel) tabelaAdd.getModel();
        modelo1.setNumRows(0);

        for(int i = 0; i < servicosAdd.size(); i++){
            modelo1.addRow(new Object[]{
                servicosAdd.get(i)
            });
        }

        DefaultTableModel modelo2 = (DefaultTableModel) tabelaDisp.getModel();
        modelo2.setNumRows(0);

        for(int i = 0; i < servicosDisp.size(); i++){
            modelo2.addRow(new Object[]{
                servicosDisp.get(i)
            });
        }
    }
    
    public void validaCamposAlt(String nome, String cpf, String email, String telefone, String usuario, String senha, String confirmaSenha, int linhasServico, String codigo){
        Funcionario funcionario = validaCampos(telaAltera, nome, cpf, email, telefone, usuario, senha, confirmaSenha, linhasServico);
        if(funcionario == null){
        }else{
            validaAlteracao(funcionario, codigo);
        }
    }
    
    public void validaAlteracao(Funcionario funcionario, String codigo){
        DaoFuncionario daoFuncionario = new DaoFuncionario();
        
        daoFuncionario.alterar(funcionario, codigo);
        telaAltera.caixaMensagem("Funcionário atualizado com sucesso!", "Funcionário atualizado", 1);
    
    }
    
    //DESATIVAR    
    public void confirma(String codigo){
        int escolha = telaConsulta.caixaOpcao("Certeza que deseja excluir este funcionario?", "Confirmar exclusão");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoFuncionario daoAgendamento = new DaoFuncionario();
        
        daoAgendamento.desativar(codigo);
        telaConsulta.caixaMensagem("Funcionario excluido com sucesso!", "Funcionario excluido", 1);
    }
}
