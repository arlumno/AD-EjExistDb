/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.ejexistdb;

import static ad.ejexistdb.XQJ.conexXQJ;
import java.io.File;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQMetaData;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Service;
import org.xmldb.api.base.XMLDBException;
import utilidades.Utils;

/**
 *
 * @author a20armandocb
 */
class AccionesApp {

    public void ejercicioXPATH() {
        XQConnection conexion = conexXQJ();
        String query;
        StringBuilder resultado = new StringBuilder();
        System.out.println("\n************************************************************************");
        System.out.println("******************************* XPATH **********************************");
        System.out.println("************************************************************************\n");
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
        Collection colBase = null;
        String query;
        System.out.println("\n************************************************************************");
        System.out.println("******************************* XMLDB **********************************");
        System.out.println("************************************************************************\n");
        try {
            StringBuilder info = new StringBuilder();
            colBase = XMLDB.obtenColeccion("/ColeccionesXML");
            info.append("\n" + "Colección actual: " + colBase.getName());

            int numDocs = colBase.getResourceCount();
            info.append("\n" + numDocs + " documentos.");
            if (numDocs > 0) {
                String nomDocs[] = colBase.listResources();
                for (int i = 0; i < numDocs; i++) {
                    info.append("\n" + "\t" + nomDocs[i]);
                }
            }

            Service servicios[] = colBase.getServices();
            info.append("\n" + "Servicios proporcionados por colección " + colBase.getName() + ":");
            for (int i = 0; i < servicios.length; i++) {
                info.append("\n" + "\t" + servicios[i].getName() + " - Versión: " + servicios[i].getVersion());
            }

//            System.out.println(info.toString());
            //ver empleados
            query = "for $emp in /EMPLEADOS/EMP_ROW[DEPT_NO = 10] return $emp";
            System.out.println("\n**********1. Ver empleados 10 **********");
            System.out.println(XMLDB.consultarQueryService(query, colBase));

            System.out.println("\n**********2.1 Ver colecciones **********");
            verColecciones(colBase);
            //ver colecciones

            //ver recursos de las colecciones
            System.out.println("\n**********2.2 Ver recursos colecciones **********");
            verRecursos(colBase);

            System.out.println("\n**********3.1 Crear coleccion **********");
            XMLDB.consultarManagementService("ejemploCreacion", XMLDB.CREATE_COLLECTION, colBase);
            verColecciones(colBase);

            System.out.println("\n**********3.2 Subir Fichero**********");
            Collection colAux = colBase.getChildCollection("ejemploCreacion");
            XMLDB.consultarManagementService("ejemploSubirArchivo.xml", XMLDB.UPLOAD_RESOURCE, colAux);
            verRecursos(colAux);

            System.out.println("\n**********3.3 Borrar Fichero**********");
            XMLDB.consultarManagementService("ejemploSubirArchivo.xml", XMLDB.DELETE_RESOURCE, colAux);
            verRecursos(colAux);

            System.out.println("\n**********3.4 Borrar Colección**********");
            XMLDB.consultarManagementService("ejemploCreacion", XMLDB.DELETE_COLLECTION, colBase);
            verColecciones(colBase);

            System.out.println("\n**********7. Actualizar Stock Productos **********");
            colAux = colBase.getChildCollection("ColeccionPruebas");
            query = "for $producto in /productos/produc\n" +
                    "let $stock :=  $producto/stock_actual\n" +
                    "return update value $producto/stock_actual with $stock+10  ";
            XMLDB.consultarQueryService(query, colAux);
            System.out.println(XMLDB.consultarQueryService("//productos/produc/stock_actual", colAux));
            
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (colBase != null) {
                    colBase.close();
                }
            } catch (XMLDBException e) {
                e.printStackTrace();
            }
        }
    }

    private void verRecursos(Collection col) throws XMLDBException {
        System.out.println("Colección actual: " + col.getName());
//            numDocs = col.getResourceCount();
        System.out.println(col.getResourceCount() + " documentos.");
        for (String coleccion : col.listChildCollections()) {
            System.out.println("\t" + coleccion + " (" + col.getChildCollection(coleccion).getResourceCount() + ")");
            for (String recurso : col.getChildCollection(coleccion).listResources()) {
                System.out.println("\t\t" + recurso);
            }

        }
    }

    private void verColecciones(Collection col) throws XMLDBException {
        System.out.println("Colección actual: " + col.getName());
        System.out.println(col.getChildCollectionCount() + " colecciones hijas.");
//            String nomHijas[] = col.listChildCollections();
        for (String coleccion : col.listChildCollections()) {
            System.out.println("\t" + coleccion);
        }
    }

    public void ejercicioXQJ() {
        XQConnection conexion = conexXQJ();
        String query;
        StringBuilder resultado = new StringBuilder();
        String coleccion = "collection('/db/ColeccionesXML/ColeccionPruebas')";
        
        System.out.println("\n************************************************************************");
        System.out.println("******************************** XQJ ***********************************");
        System.out.println("************************************************************************\n");
        
        try {
            if (conexion != null) {

                System.out.println("\n**********1. Ver Productos **********");
                query = coleccion + "/productos/produc";
                System.out.println(XQJ.consultarXQuery(query, conexion));
                
                System.out.println("\n**********2. Contar Productos **********");
                query = coleccion + "/productos/count(produc[precio > 50])";
                System.out.println(XQJ.consultarXQuery(query, conexion));
                
                System.out.println("\n**********3. Productos por zona **********");
                query = "for $zona in distinct-values("+coleccion+"/productos//cod_zona/data())\n" +
                                    "let $productosZona := count("+coleccion+"/productos/produc[cod_zona = $zona])\n" +
                                    "return $productosZona";
                System.out.println(XQJ.consultarXQuery(query, conexion));
                
                                                
                System.out.println("\n**********4. Consulta desde fichero **********");
                query = Utils.leerArchivo(new File("src/ad/ejexistdb/miconsulta.xq"));   // pasa un archivo a string.             
                System.out.println(XQJ.consultarXQuery(query, conexion));
                
//                System.out.println("\n**********5. Crear empleado 10 **********");
//                query = coleccion + "";
//                System.out.println(XQJ.consultarXQuery(query, conexion));
            }

        } catch (XQException ex) {
            ex.printStackTrace();

        }
    }
}
