/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ad.ejexistdb;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQMetaData;
import javax.xml.xquery.XQResultSequence;

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
        XQConnection conexion = connectionExistDb();
        String query;
        if (conexion != null) {
            try {
                XQMetaData xqmd = conexion.getMetaData();
                System.out.println("Conexión establecida como: " + xqmd.getUserName());
                System.out.println("Producto: " + xqmd.getProductName() + ", " + "versión: " + xqmd.getXQJMajorVersion() + "." + xqmd.getXQJMinorVersion() + ".\n"
                        + "Soporte para transacciones: " + (xqmd.isTransactionSupported() ? "Sí" : "No") + ".\n"
                        + "Validación con esquemas: " + (xqmd.isSchemaValidationFeatureSupported() ? "Sí" : "No") + ".\n"
                        + "Serialización: " + (xqmd.isSerializationFeatureSupported() ? "Sí" : "No") + ".\n"
                        + "\n*************************************************************\n\n"
                );

//                XQExpression q = conexion.createExpression();
//                XQResultSequence resultQ = q.executeQuery(query);
//                XQResultSequence resultQ = conexion.createExpression().executeQuery(query);
//
//                while (resultQ.next()) {
//                    System.out.println(resultQ.getItemAsString(null));
//                }
//              
    
                query = "//bib/libro";
                System.out.println("--- Todos los libros: ---");
                System.out.println(consultarXQuery(query, conexion));
                
                query = "count(//bib/libro)";
                System.out.println("--- Numero de libros: ---");
                System.out.println(consultarXQuery(query, conexion));
           
                query = "//bib/count(libro)";                
                System.out.println(consultarXQuery(query, conexion));
//                
                System.out.println("--- Consultas Extra: ---");
                query = "for $libro in /bib/libro\n"
                        + "	let $year := $libro/@año, $titulo := $libro/titulo order by  $libro/@año\n"
                        + "	return \n"
                        + "	concat($year, \", \", $titulo)";
                System.out.println(consultarXQuery(query, conexion));

                query = "for $titulos in /bib/libro/titulo/string() \n"
                        + "	return \n"
                        + "	$titulos";
                System.out.println(consultarXQuery(query, conexion));

                query = "for $libro in /bib/libro \n"
                        + "	return\n"
                        + "	if ($libro/precio = \"65.95\") then\n"
                        + "		$libro\n"
                        + "		else()";
                System.out.println(consultarXQuery(query, conexion));

                query = "for $libro in /bib/libro\n"
                        + "	let $titulo := $libro/titulo, $autor := $libro/autor\n"
                        + "	return\n"
                        + "	<libro>{concat($titulo,\", \", count($autor))}</libro>";
                System.out.println(consultarXQuery(query, conexion));

            } catch (XQException e) {
                muestraErrorXQuery(e);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conexion != null) {
                        conexion.close();
                    }
                } catch (XQException xe) {
                    xe.printStackTrace();
                }
            }
        } else {
            System.out.println("Error al conectar.");
        }
    }

    public static XQConnection connectionExistDb() {
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

    private static void muestraErrorXQuery(XQException e) {
        System.err.println("XQuery ERROR mensaje: " + e.getMessage());
        System.err.println("XQuery ERROR causa: " + e.getCause());
        System.err.println("XQuery ERROR código: " + e.getVendorCode());
    }

    private static String consultarXQuery(String consulta, XQConnection conexion) throws XQException {
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
