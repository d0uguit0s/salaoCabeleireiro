/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DAO.DaoLogin;
import View.Cliente.MenuCliente;
import View.Funcionario.MenuFuncionario;
import View.Login;

/**
 *
 * @author Douglas
 */
public class ControllerLogin {
    private final Login tela;
    
    public ControllerLogin(Login tela){
        this.tela = tela;
    }
    
    public void validaCampos(String usuario, String senha, String tipo){
        if(usuario.isEmpty() && senha.isEmpty()){
            tela.tremeTela();
            tela.alteraCorCampoVazio(tela.getjTextFieldUsuario());
            tela.alteraCorCampoVazio(tela.getjPasswordFieldSenha());
        }else if(usuario.isEmpty()){
            tela.tremeTela();
            tela.alteraCorCampoVazio(tela.getjTextFieldUsuario());
        }else if(senha.isEmpty()){
            tela.tremeTela();
            tela.alteraCorCampoVazio(tela.getjPasswordFieldSenha());
        }else{
            Model.Login login = new Model.Login(usuario, senha);
            validaLogin(login, tipo);
        }
    }
    
    public void validaLogin(Model.Login login, String tipo){
        DaoLogin daoLogin = new DaoLogin();
        if(daoLogin.validar(login) && "CLIENTE".equals(tipo)){
            if(daoLogin.isCliente(login)){
                tela.dispose();
                //tela.caixaMensagem("Cliente Logou", "Logado com sucesso!", 1);
                new MenuCliente(daoLogin.getNomeCliente(login), tela.getjTextFieldUsuario().getText(), tela.getjPasswordFieldSenha().getText()).setVisible(true);
            }else{
                tela.caixaMensagem("Cliente não encontrado!", "Tipo incorreto", 0);
            }
        }else if(daoLogin.validar(login) && "FUNCIONARIO".equals(tipo)){
            if(daoLogin.isFuncionario(login)){
                tela.dispose();
                //tela.caixaMensagem("Funcionário Logou", "Logado com sucesso!", 1);
                new MenuFuncionario(daoLogin.getInfoFuncionario(login)).setVisible(true);
            }else{
                tela.caixaMensagem("Funcionário não encontrado!", "Tipo incorreto", 0);
            }                
        }else{
            tela.caixaMensagem("Por favor verifique suas credenciais de tente novamente", "Erro de credencial", 0);
            tela.setVisible(true);
        }
    }
}
