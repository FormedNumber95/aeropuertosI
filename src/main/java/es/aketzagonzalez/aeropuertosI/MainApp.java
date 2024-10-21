package es.aketzagonzalez.aeropuertosI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import db.ConexionBBDD;

// TODO: Auto-generated Javadoc
/**
 * Clase MainApp.
 */
public class MainApp extends Application {
    
    /** El stage. */
    private static Stage stage;

    /**
     * Metodo que pone la imagen en el header, establece el idioma y llama al setRoot de 3 parametros.
     *
     * @param s El stage
     * @throws IOException Si hay algun error de entrada/salida.
     */
    @Override
    public void start(@SuppressWarnings("exports") Stage s) throws IOException {
    	Properties connConfig =ConexionBBDD.loadProperties() ;
        String lang = connConfig.getProperty("language");
        Locale locale = new Locale.Builder().setLanguage(lang).build();
        ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);
        stage=s;
		Image imagen=new Image(getClass().getResource("/imagenes/agenda.png").toString());
        stage.getIcons().add(imagen);
		setRoot("tablaPersonas","Personas",bundle);
    }

    /**
     * Establece the root llamando al setRoot de 3 parametros.
     *
     * @param fxml El nuevo root
     * @param bundle the bundle
     * @throws IOException Si hay algun error de entrada/salida.
     */
    static void setRoot(String fxml,ResourceBundle bundle) throws IOException {
        setRoot(fxml,stage.getTitle(),bundle);
    }

    /**
     * Establece el root, el bundle y el titulo del stage.
     *
     * @param fxml El fxml
     * @param title El titulo
     * @param bundle the bundle
     * @throws IOException Si hay algun error de entrada/salida.
     */
    static void setRoot(String fxml, String title,ResourceBundle bundle) throws IOException {
        Scene scene = new Scene(loadFXML(fxml,bundle));
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Carga el FXML.
     *
     * @param fxml El fxml
     * @param bundle the bundle
     * @return El fxml cargado
     * @throws IOException Señal de que hay un error de entrada/salida.
     */
    private static Parent loadFXML(String fxml,ResourceBundle bundle) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"),bundle);
        return fxmlLoader.load();
    }


    /**
     * El metodo main que lanza el programa.
     *
     * @param args los argumetnos que recive por consola, ninguno
     * @throws SQLException the SQL exception
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }

    /**
     * Getter del stage.
     *
     * @return El stage
     */
    public static Stage getStage() {
		return stage;
	}
    
}
