/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.ejexistdb;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;
import net.xqj.exist.ExistXQDataSource;

/**
 *
 * @author a20armandocb
 */
public class XQJ {
    
    public static XQConnection conexXQJ() {
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

    public static void muestraErrorXQuery(XQException e) {
        System.err.println("XQuery ERROR mensaje: " + e.getMessage());
        System.err.println("XQuery ERROR causa: " + e.getCause());
        System.err.println("XQuery ERROR código: " + e.getVendorCode());
    }

    public static String consultarXQuery(String consulta, XQConnection conexion) throws XQException {
        StringBuilder resultado = new StringBuilder();
        resultado.append("*****************************\n");
        resultado.append("[Consulta: " + consulta + "]\n");
        XQResultSequence resultQ = conexion.createExpression().executeQuery(consulta);
        resultado.append("**Resultado:** \n");
        while (resultQ.next()) {
            resultado.append(resultQ.getItemAsString(null) + "\n");
        }
        resultado.append("*****************************\n");
        return resultado.toString();
    }
    
    
}
