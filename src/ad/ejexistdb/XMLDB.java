/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.ejexistdb;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XPathQueryService;

/**
 *
 * @author a20armandocb
 */
public class XMLDB {

    public static final String CREATE_COLLECTION = "CREATE_COLLECTION";
    public static final String DELETE_COLLECTION = "DELETE_COLLECTION";
    public static final String UPLOAD_RESOURCE = "UPLOAD_RESOURCE";
    public static final String DELETE_RESOURCE = "DELETE_RESOURCE";

    public static Collection obtenColeccion(String nomCol) throws Exception {
        Database dbDriver;
        Collection col;
        dbDriver = (Database) Class.forName("org.exist.xmldb.DatabaseImpl").newInstance();
        DatabaseManager.registerDatabase(dbDriver);
        col = DatabaseManager.getCollection(
                "xmldb:exist://localhost:8080/exist/xmlrpc/db" + nomCol,
                "admin", "");
        return col;
    }

    static String consultarQueryService(String query, Collection col) {
        StringBuilder resultado = new StringBuilder();
        try {
            XPathQueryService service = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            ResourceSet result = service.query(query);
            ResourceIterator i = result.getIterator();
            Resource r;
            while (i.hasMoreResources()) {
                r = (Resource) i.nextResource();
                resultado.append(r.getContent());
            }

        } catch (XMLDBException ex) {
            Logger.getLogger(XMLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toString();
    }

    /**
     *
     * @param resourceName nombre de la coleccion o de la ruta del archivo
     * @param operation
     * @param col coleccion sobre la que operar.
     * @return
     */
    static String consultarManagementService(String resourceName, String operation, Collection col) {
        StringBuilder resultado = new StringBuilder();
        Resource resource;
        File file;
        try {
            CollectionManagementService service = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");
            switch (operation) {
                case CREATE_COLLECTION:
                    service.createCollection(resourceName);
                    break;

                case DELETE_COLLECTION:
                    service.removeCollection(resourceName);
                    break;

                case UPLOAD_RESOURCE:
                    file = new File(resourceName);
                    if(file.exists()){
                        
                    resource = col.createResource(file.getName(), "XMLResource");
                    resource.setContent(file);
                    col.storeResource(resource);
                    }else{
                        System.out.println("Operación NO realizada. El archivo "+resourceName+"no existe.");
                    }
                    break;
                    
                case DELETE_RESOURCE:
                    resource = col.getResource(resourceName);
                    col.removeResource(resource);
                    break;
                default:
            }

        } catch (XMLDBException ex) {
            Logger.getLogger(XMLDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado.toString();
    }
}
