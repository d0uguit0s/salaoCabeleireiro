/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoAgendamento;
import DAO.DaoServico;
import Model.Agendamento;
import Model.Servico;
import View.Agendamento.Altera_Agendamento;
import View.Agendamento.Consulta_Agendamento_Cliente;
import View.Agendamento.Consulta_Agendamento_Funcionario;
import View.Agendamento.Consulta_Agendamento_Geral;
import View.Agendamento.Reg_Agendamento;
import View.Login;
import View.Servico.Altera_Servico;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Douglas
 */
public class ControllerAgendamento {
    public final Reg_Agendamento telaRegistro;
    public final Consulta_Agendamento_Cliente telaConsultaCliente;
    public final Consulta_Agendamento_Funcionario telaConsultaFuncionario;
    public final Consulta_Agendamento_Geral telaConsultaGeral;
    public final Altera_Agendamento telaAltera;
    public ArrayList<ArrayList<String>> servicosDisp = new ArrayList<>();
    public ArrayList<ArrayList<String>> servicosAdd = new ArrayList<>();
    
    public ControllerAgendamento(Reg_Agendamento telaRegistro, Consulta_Agendamento_Cliente telaConsultaCliente, Consulta_Agendamento_Funcionario telaConsultaFuncionario, Consulta_Agendamento_Geral telaConsultaGeral, Altera_Agendamento telaAltera){
        this.telaRegistro = telaRegistro;
        this.telaConsultaCliente = telaConsultaCliente;
        this.telaConsultaFuncionario = telaConsultaFuncionario;
        this.telaConsultaGeral = telaConsultaGeral;
        this.telaAltera = telaAltera;
        
    }
    
    //UTIL
    public Agendamento validaCampos(Reg_Agendamento telaCadastro, String data, String hora, String usuario, String senha, int linhasServico){
        if(data.isEmpty() || hora.isEmpty()){
            telaCadastro.tremeTela();
            if(data.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldData());
            }
            if(hora.isEmpty()){
                telaCadastro.alteraCorCampoVazio(telaCadastro.getjFormattedTextFieldHora());
            }
            if(linhasServico < 1){
                telaCadastro.caixaMensagem("Escolha ao menos um serviço!", "Escolher serviço", 0);
            }
            return null;
        }else{
            DaoAgendamento daoAgendamento = new DaoAgendamento();
            ArrayList<Servico> servicos = new ArrayList<>();
            System.out.println(servicosAdd);
            servicosAdd.forEach((servicoUni) -> {
                ResultSet rs = daoAgendamento.carregaServicos(servicoUni.get(0), servicoUni.get(3));
                try {
                    while(rs.next()){
                        Servico servico = new Servico(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4));
                        servicos.add(servico);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            
            String[] dataEdita = data.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Model.Login credenciais = new Model.Login(usuario, senha);
            Agendamento agendamento = new Agendamento(credenciais, dataBanco, hora, servicos);
            return agendamento;
        }
    }
    
    public Agendamento validaCampos(Altera_Agendamento telaAltera, String data, String hora, String usuario, String senha, int linhasServico){
        if(data.isEmpty() || hora.isEmpty()){
            if(data.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjFormattedTextFieldData());
            }
            if(hora.isEmpty()){
                telaAltera.tremeTela();
                telaAltera.alteraCorCampoVazio(telaAltera.getjFormattedTextFieldHora());
            }
            if(linhasServico < 1){
                telaAltera.caixaMensagem("Escolha ao menos um serviço!", "Escolher serviço", 0);
            }
            return null;
        }else{
            DaoAgendamento daoAgendamento = new DaoAgendamento();
            ArrayList<Servico> servicos = new ArrayList<>();
            System.out.println(servicosAdd);
            servicosAdd.forEach((servicoUni) -> {
                ResultSet rs = daoAgendamento.carregaServicos(servicoUni.get(0), servicoUni.get(3));
                try {
                    while(rs.next()){
                        Servico servico = new Servico(rs.getString(1), rs.getDouble(2), rs.getString(3), rs.getString(4));
                        servicos.add(servico);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            
            String[] dataEdita = data.split("/");
            String dataBanco = dataEdita[2] + '-' + dataEdita[1] + '-' + dataEdita[0];
            Model.Login credenciais = new Model.Login(usuario, senha);
            Agendamento agendamento = new Agendamento(credenciais, dataBanco, hora, servicos);
            return agendamento;
        }
    }
    
    //CADASTRAR
    public String carregarCliente(String codigo){
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        return daoAgendamento.getNomeCliente(codigo);
    }
    
    public void carregarListaServicos(JTable tabelaServicosDisp){
        DefaultTableModel modelo = (DefaultTableModel) tabelaServicosDisp.getModel();
        modelo.setNumRows(0);
        
        DaoServico daoServico = new DaoServico();
        ResultSet rs = daoServico.listarServicosDisponiveis();
        
        try{
            while(rs.next()){
                modelo.addRow(new Object[]{
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                });
                ArrayList<String> servicos = new ArrayList<>();
                servicos.add(rs.getString(2));
                servicos.add(rs.getString(3));
                servicos.add(rs.getString(4));
                servicos.add(rs.getString(5));
                
                servicosDisp.add(servicos);
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
        //System.out.println(servicosDisp);
    }
    
    public void adicionaServicos(JTable tabelaServicosDisp, JTable tabelaServicosAdd){
        ArrayList<String> servicos = new ArrayList<>();
        int linha = tabelaServicosDisp.getSelectedRow();
        if(linha > -1){
            servicos.add((String) tabelaServicosDisp.getValueAt(linha, 0));
            servicos.add((String) tabelaServicosDisp.getValueAt(linha, 1));
            servicos.add((String) tabelaServicosDisp.getValueAt(linha, 2));
            servicos.add((String) tabelaServicosDisp.getValueAt(linha, 3));
        
            //System.out.println(servico);

            servicosAdd.add(servicos);
            servicosDisp.remove(servicos);

            //System.out.println(servicosAdd);
            //System.out.println(servicosDisp);

            DefaultTableModel modelo = (DefaultTableModel) tabelaServicosAdd.getModel();
            modelo.setNumRows(0);

            for(int i = 0; i < servicosAdd.size(); i++){
                modelo.addRow(new Object[]{
                    servicosAdd.get(i).get(0),
                    servicosAdd.get(i).get(1),
                    servicosAdd.get(i).get(2),
                    servicosAdd.get(i).get(3)
                });
            }

            DefaultTableModel modelo2 = (DefaultTableModel) tabelaServicosDisp.getModel();
            modelo2.removeRow(tabelaServicosDisp.getSelectedRow());
        }else{
            telaRegistro.caixaMensagem("Selecione um serviço para adicionar!", "Nenhuma linha selecionada", 0);
        }
        
    }
    
    public void removeServicos(JTable tabelaServicosDisp, JTable tabelaServicosAdd){
        ArrayList<String> servicos = new ArrayList<>();
        int linha = tabelaServicosAdd.getSelectedRow();
        //System.out.println(linha);
        if(linha > -1){
            servicos.add((String) tabelaServicosAdd.getValueAt(linha, 0));
            servicos.add((String) tabelaServicosAdd.getValueAt(linha, 1));
            servicos.add((String) tabelaServicosAdd.getValueAt(linha, 2));
            servicos.add((String) tabelaServicosAdd.getValueAt(linha, 3));

            servicosDisp.add(servicos);
            servicosAdd.remove(servicos);

            DefaultTableModel modelo = (DefaultTableModel) tabelaServicosDisp.getModel();
            modelo.setNumRows(0);

            for(int i = 0; i < servicosDisp.size(); i++){
                modelo.addRow(new Object[]{
                    servicosDisp.get(i).get(0),
                    servicosDisp.get(i).get(1),
                    servicosDisp.get(i).get(2),
                    servicosDisp.get(i).get(3)
                });
            }

            DefaultTableModel modelo2 = (DefaultTableModel) tabelaServicosAdd.getModel();
            modelo2.removeRow(tabelaServicosAdd.getSelectedRow());
        }else{
            telaRegistro.caixaMensagem("Selecione um serviço para remover!", "Nenhuma linha selecionada", 0);
        }        
    }
    
    public void validaCamposCad(String data, String hora, String codigo, String usuario, String senha, int linhaServico){
        Agendamento agendamento = validaCampos(telaRegistro, data, hora, usuario, senha, linhaServico);
        //System.out.println(agendamento.getServicos());
        if(agendamento == null){
        }else{
            validaCadastro(agendamento, codigo);
        }
    }
    
    public void validaCadastro(Agendamento agendamento, String codigo){
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        daoAgendamento.adicionar(agendamento, codigo);
        telaRegistro.caixaMensagem("Atendimento agendado com sucesso!", "Serviço cadastrado", 1);
        telaRegistro.dispose();
    }
    
    
    //LISTAR
    public void carregarAgendaCliente(JTable tableServico, String codigo){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        ResultSet rs1 = daoAgendamento.listarAgendaCliente(codigo);
        
        try{            
            while(rs1.next()){
                String registroServico = "";
                ResultSet rs2 = daoAgendamento.listarServicosCliente(rs1.getInt(1));
                while(rs2.next()){
                    if(rs2.isLast()){
                        registroServico += rs2.getString(1) + "->" + rs2.getString(2);
                    }else{
                        registroServico += rs2.getString(1) + "->" + rs2.getString(2) + ", ";
                    }
                }
                //System.out.println(registroServico);  
                String[] dataBanco = rs1.getString(2).split("-");
                String data = dataBanco[2] + '/' + dataBanco[1] + '/' + dataBanco[0];
                modelo.addRow(new Object[]{
                    rs1.getString(1),
                    data,
                    rs1.getString(3),
                    registroServico
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    public void carregarAgendaFuncionario(JTable tableServico, String codigo){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        ResultSet rs1 = daoAgendamento.listarAgendaFuncionario(codigo);
        
        try{
            while(rs1.next()){       
                String registroServico = "";
                ResultSet rs2 = daoAgendamento.listarServicosFuncionario(codigo, rs1.getString(1));
                while(rs2.next()){
                    if(rs2.isLast()){
                        registroServico += rs2.getString(1);
                    }else{
                        registroServico += rs2.getString(1) + ", ";
                    }
                }
                
                String[] dataBanco = rs1.getString(3).split("-");
                String data = dataBanco[2] + '/' + dataBanco[1] + '/' + dataBanco[0];
                modelo.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    data,
                    rs1.getString(4),
                    registroServico
                });
            }
        }catch(SQLException error){
            throw new RuntimeException(error);
        }
    }
    
    public void carregarAgendaGeral(JTable tableServico){
        DefaultTableModel modelo = (DefaultTableModel) tableServico.getModel();
        modelo.setNumRows(0);
        
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        ResultSet rs1 = daoAgendamento.listarAgendaGeral();
        
        try{
            while(rs1.next()){       
                String registroServico = "";
                ResultSet rs2 = daoAgendamento.listarServicosGeral(rs1.getString(1));
                while(rs2.next()){
                    if(rs2.isLast()){
                        registroServico += rs2.getString(1);
                    }else{
                        registroServico += rs2.getString(1) + ", ";
                    }
                }                
                String[] dataBanco = rs1.getString(3).split("-");
                String data = dataBanco[2] + '/' + dataBanco[1] + '/' + dataBanco[0];
                modelo.addRow(new Object[]{
                    rs1.getString(1),
                    rs1.getString(2),
                    data,
                    rs1.getString(4),
                    registroServico
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
            telaConsultaCliente.caixaMensagem("Escolha uma linha primeiro", "Nenhuma linha selecionada", 0);
        }else{
            dados.add((String) tabela.getValueAt(linha, 0));
            dados.add((String) tabela.getValueAt(linha, 1));
            dados.add((String) tabela.getValueAt(linha, 2));
            dados.add((String) tabela.getValueAt(linha, 3));
        }
        //System.out.println(dados);
        return dados;
    }
    
    public void carregarRegistro(ArrayList<String> dados, JTable tabelaDisp, JTable tabelaAdd){
        telaAltera.preencheCampos(dados.get(0), dados.get(1), dados.get(2));

        String servicos = dados.get(3);
        String[] adicionados = servicos.split(", ");
        //System.out.println("adicionados:" + Arrays.toString(adicionados));
        ArrayList<ArrayList<String>> registro = new ArrayList<>();
        for (String adicionado : adicionados) {
            ArrayList<ArrayList<String>> copiaDisp = servicosDisp;
            copiaDisp.forEach((servico) -> {
                //System.out.println(servico);
                //System.out.println(servicosDisp);
                if(servico.get(0).equals(adicionado.split("->")[0])){
                    //System.out.println("passou pelo serviço");
                    if(servico.get(3).equals(adicionado.split("->")[1])){
                        //System.out.println("passou pelo funcionario");
                        ArrayList<String> dado = new ArrayList<>();
                        dado.add(servico.get(0));
                        dado.add(servico.get(1));
                        dado.add(servico.get(2));
                        dado.add(servico.get(3));
                        registro.add(dado);
                    }
                }
            });
        }
        registro.forEach((r) -> {
            servicosAdd.add(r);
            servicosDisp.remove(r);
        });
        //System.out.println(registro);
        //System.out.println(servicosAdd);
        //System.out.println(servicosDisp);
        organizarTabelas(tabelaDisp, tabelaAdd);
        //System.out.println(servicosDisp);
        //System.out.println(servicosAdd);
    }
    
    public void organizarTabelas(JTable tabelaDisp, JTable tabelaAdd){
        //System.out.println(servicosAdd);
        //System.out.println(servicosDisp);
        
        DefaultTableModel modelo1 = (DefaultTableModel) tabelaAdd.getModel();
        modelo1.setNumRows(0);

        for(int i = 0; i < servicosAdd.size(); i++){
            modelo1.addRow(new Object[]{
                servicosAdd.get(i).get(0),
                servicosAdd.get(i).get(1),
                servicosAdd.get(i).get(2),
                servicosAdd.get(i).get(3)
            });
        }

        DefaultTableModel modelo2 = (DefaultTableModel) tabelaDisp.getModel();
        modelo2.setNumRows(0);

        for(int i = 0; i < servicosDisp.size(); i++){
            modelo2.addRow(new Object[]{
                servicosDisp.get(i).get(0),
                servicosDisp.get(i).get(1),
                servicosDisp.get(i).get(2),
                servicosDisp.get(i).get(3)
            });
        }
    }
    
    public void validaCamposAlt(String data, String hora, String codigo, String usuario, String senha, int linhasServico){
        Agendamento agendamento = validaCampos(telaAltera, data, hora, usuario, senha, linhasServico);
        if(agendamento == null){
        }else{
            validaAlteracao(agendamento, codigo);
        }
    }
    
    public void validaAlteracao(Agendamento agendamento, String codigo){
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        
        daoAgendamento.alterar(agendamento, codigo);
        telaAltera.caixaMensagem("Funcionário atualizado com sucesso!", "Funcionário atualizado", 1);
        telaAltera.dispose();
    }
    
    //DESATIVAR
    public void confirma(String codigo){
        int escolha = telaConsultaCliente.caixaOpcao("Certeza que deseja cancelar este agendamento?", "Confirmar cancelamento");
        if(escolha == 0){
            desativar(codigo);
        }
    }
    
    public void desativar(String codigo){
        DaoAgendamento daoAgendamento = new DaoAgendamento();
        
        daoAgendamento.desativar(codigo);
        telaConsultaCliente.caixaMensagem("Consulta cancelada com sucesso!", "Consulta cancelada", 1);
    }
}
