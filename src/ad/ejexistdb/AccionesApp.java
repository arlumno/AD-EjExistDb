/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.ejexistdb;

import static ad.ejexistdb.ADEjExistDb.conexXQJ;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQMetaData;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import textos.SalidasGui;

/**
 *
 * @author a20armandocb
 */
class AccionesApp {

    public void ejercicioXPATH() {
        XQConnection conexion = conexXQJ();
        String query;
        StringBuilder resultado = new StringBuilder();
        if (conexion != null) {
            try {
                XQMetaData xqmd = conexion.getMetaData();
                resultado.append("\nConexión establecida como: " + xqmd.getUserName());
                resultado.append("\nProducto: " + xqmd.getProductName() + ", " + "versión: " + xqmd.getXQJMajorVersion() + "." + xqmd.getXQJMinorVersion() + ".\n"
                        + "Soporte para transacciones: " + (xqmd.isTransactionSupported() ? "Sí" : "No") + ".\n"
                        + "Validación con esquemas: " + (xqmd.isSchemaValidationFeatureSupported() ? "Sí" : "No") + ".\n"
                        + "Serialización: " + (xqmd.isSerializationFeatureSupported() ? "Sí" : "No") + ".\n"
                        + "\n*************************************************************\n\n"
                );

                query = "//bib/libro";
                resultado.append("\n--- Todos los libros: ---");
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                query = "//bib/libro/titulo/string()";
                resultado.append("\n--- Todos los libros (titulos): ---");
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                query = "count(//bib/libro)";
                resultado.append("\n--- Numero de libros: ---");
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                resultado.append("\n--- Consultas Extra: ---");
                query = "for $libro in /bib/libro\n"
                        + "	let $year := $libro/@año, $titulo := $libro/titulo order by  $libro/@año\n"
                        + "	return \n"
                        + "	concat($year, \", \", $titulo)";
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                query = "for $titulos in /bib/libro/titulo/string() \n"
                        + "	return \n"
                        + "	$titulos";
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                query = "for $libro in /bib/libro \n"
                        + "	return\n"
                        + "	if ($libro/precio = \"65.95\") then\n"
                        + "		$libro\n"
                        + "		else()";
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

                query = "for $libro in /bib/libro\n"
                        + "	let $titulo := $libro/titulo, $autor := $libro/autor\n"
                        + "	return\n"
                        + "	<libro>{concat($titulo,\", \", count($autor))}</libro>";
                resultado.append("\n" + XQJ.consultarXQuery(query, conexion));

//                peticiones.SalidasGui.bloqueTexto(resultado.toString()); //ver en consola, ya que la ventana de swing interpreta los tags xml
                System.out.println(resultado.toString());

            } catch (XQException e) {
                XQJ.muestraErrorXQuery(e);
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
            resultado.append("\nError al conectar.");
        }
    }

    public void ejercicioXMLDB() {
        Collection col = null;
        try {
            StringBuilder info = new StringBuilder();
            col = XMLDB.obtenColeccion("/ColeccionesXML");
            info.append("\n" + "Colección actual: " + col.getName());

            int numDocs = col.getResourceCount();
            info.append("\n" + numDocs + " documentos.");
            if (numDocs > 0) {
                String nomDocs[] = col.listResources();
                for (int i = 0; i < numDocs; i++) {
                    info.append("\n" + "\t" + nomDocs[i]);
                }
            }

            Service servicios[] = col.getServices();
            info.append("\n" + "Servicios proporcionados por colección " + col.getName() + ":");
            for (int i = 0; i < servicios.length; i++) {
                info.append("\n" + "\t" + servicios[i].getName() + " - Versión: " + servicios[i].getVersion());
            }

//            System.out.println(info.toString());
            //ver empleados
            String query = "for $emp in /EMPLEADOS/EMP_ROW[DEPT_NO = 10] return $emp";
            System.out.println("\n********** Ver empleados 10 **********");
            System.out.println(XMLDB.consultarQueryService(query, col));

            //ver colecciones
            System.out.println("\n********** Ver colecciones **********");
            System.out.println("Colección actual: " + col.getName());
            System.out.println(col.getChildCollectionCount() + " colecciones hijas.");
//            String nomHijas[] = col.listChildCollections();
            for (String coleccion : col.listChildCollections()) {
                System.out.println("\t" + coleccion);
            }

            //ver recursos de las colecciones
            System.out.println("\n********** Ver recursos colecciones **********");
            System.out.println("Colección actual: " + col.getName());
//            numDocs = col.getResourceCount();
            System.out.println(numDocs + " documentos.");
            for (String coleccion : col.listChildCollections()) {
                System.out.println("\t" + coleccion + " (" + col.getChildCollection(coleccion).getResourceCount() + ")");
                for (String recurso : col.getChildCollection(coleccion).listResources()) {
                    System.out.println("\t\t" + recurso);
                }

            }
//            if (numDocs > 0) {
//                String nomDocs[] = col.listResources();
//                for (int i = 0; i < numDocs; i++) {
//                    System.out.println("\t" + nomDocs[i]);
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (col != null) {
                    col.close();
                }
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    public void ejercicioXQJ() {
    }
}
