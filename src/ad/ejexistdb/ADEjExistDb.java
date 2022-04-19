/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ad.ejexistdb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import net.xqj.exist.ExistXQDataSource;

/**
 *
 * @author a20armandocb
 */
public class ADEjExistDb {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(connectionExistDb()){            
            
        }else{
            System.out.println("Error de conexión");
        }
    }
    public static XQConnection connectionExistDb(){
        try {
                XQDataSource server = new ExistXQDataSource();
                server.setProperty("serverName", "localhost");
                server.setProperty("port", "8080");
                server.setProperty("user", "admin");
                server.setProperty("password", "");
                return server.getConnection();
            } catch (XQException ex) {
                
            }
        return null;
    }
    
}
