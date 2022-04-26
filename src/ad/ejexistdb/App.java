
package ad.ejexistdb;

import menu.Menu;
import utilidades.Log;

/**
 *
 * @author a20armandocb
 */
public class App {

    
    /**
     * @param args the command line arguments
     */
    static private Log log = Log.getInstance();

    public static void main(String[] args) {
        boolean continuar = true;
        Menu menu = construirMenuPrincipal();
        
        do {
            try {
                continuar = menuAcciones(menu);
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        } while (continuar);
        System.exit(0);
    }

    private static Menu construirMenuPrincipal() {
        Menu menu = new Menu();
        AccionesApp app = new AccionesApp();


        
        menu.setTituloMenu("Menu Exist DB");
        menu.setTextoSalir("Salir");
        log.addToLog("Iniciado el programa");

      
        menu.addLabel("Ejercicio Java Xpath");
        menu.addOpcion("XPATH", () -> {
            app.ejercicioXPATH();
        });
        
        menu.addLabel("Ejercicios XMLB y XQJ");
        menu.addOpcion("ejercicio XMLB", () -> {
            app.ejercicioXMLDB();
        });
 
        menu.addOpcion("ejercicio XQJ", () -> {
            app.ejercicioXQJ();
        });
       
//        menu.addOpcion("Borrar Cuenta Plazo", () -> {

//        });
        
           
        
        
        /* Log de libreriaAr 1.4 */
        menu.addLabel("");
        menu.addLabel("");
        menu.addLabel("LOG");
        menu.addOpcion("Ver Log", () -> {
            peticiones.SalidasGui.bloqueTexto("Log", utilidades.Log.getInstance().getLog());
        });
        menu.addOpcion("Borrar Log", () -> {
            utilidades.Log.getInstance().borrarLog();
        });
        

        return menu;
    }

    private static boolean menuAcciones(Menu menu) throws Exception {
        boolean continuar = true;
        menu.mostrarGUI();
        //menu.mostrar();
        switch (menu.getSeleccion()) {
            case 0:
                //salir
                continuar = false;
                log.addToLog("finalizado el programa");
                System.out.println("Bye Bye!");
                break;

        }
        return continuar;
    }

}
