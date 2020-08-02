/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionFactory;

/**
 *
 * @author gabri
 */
public class testConn {
    
    public static void main(String[] args){
        new ConnectionFactory.Connection().getConnection();
        System.out.println("Conexão Realizada com sucesso!");
    }
}
