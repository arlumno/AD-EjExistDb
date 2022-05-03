/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.ejexistdb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static void guardarArchivoConsulta(String nombreArchivo, String consulta, XQConnection conexion) {
        BufferedWriter bw = null;
        try {
            XQResultSequence resultQ = conexion.createExpression().executeQuery(consulta);

            bw = new BufferedWriter(new FileWriter(nombreArchivo));
            bw.write("<?xml version='1.0' encoding='ISO-8859-1'?>" + "\n");

            while(resultQ.next()) {
                String cad = resultQ.getItem().getItemAsString(null);
                System.out.println(" output " + cad); // visualizamos
                bw.write(cad + "\n"); // grabamos en el fichero
            }
            bw.close(); // Cerramos el fichero el fichero

        } catch (IOException ex) {
            System.out.println(ex.toString());
        } catch (XQException ex) {
            Logger.getLogger(XQJ.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }

    }

}
