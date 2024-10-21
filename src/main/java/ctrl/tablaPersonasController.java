package ctrl;

import java.io.IOException;

import dao.DaoPersona;
import es.aketzagonzalez.aeropuertosI.MainApp;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelos.ModeloPersona;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;

/**
 * Clase tablaPersonasController.
 */
public class tablaPersonasController {

	/** El stage. */
	private static Stage s;
	
    /** El btn aniadir. */
    @FXML
    private Button btnAniadir;
    
    /** El btn eliminar. */
    @FXML
    private Button btnEliminar;

    /** El btn modificar. */
    @FXML
    private Button btnModificar;
    
    /** El btn exportar. */

    /** El id tabla apellido. */
    @FXML
    private  TableColumn<ModeloPersona, String> idTablaApellido;

    /** El id tabla edad. */
    @FXML
    private TableColumn<ModeloPersona, Integer> idTablaEdad;

    /** El id tabla nombre. */
    @FXML
    private TableColumn<ModeloPersona, String> idTablaNombre;

    /** El tabla personas. */
    @FXML
    private TableView<ModeloPersona> tablaPersonas=new TableView<ModeloPersona>(listaTodas);
    
    /** El texto del filtro. */
    @FXML
    private TextField txtFiltro;
    
    /** La lista de todas las personas. */
    private static ObservableList<ModeloPersona> listaTodas;
    
    /** El filtro. */
    private FilteredList<ModeloPersona> filtro;
    
    /** Identificador de si añade o modifica la persona. */
    private static boolean esAniadir=false;
    
    private  ContextMenu contextMenu;

    /**
     * Aniadir persona a la tabla llamando a una ventana modal.
     *
     * @param event El evento
     */
    @FXML
    void aniadirPersona(ActionEvent event) {
    	esAniadir=true;
    	s=new Stage();
    	Scene scene;
		try {
			 FXMLLoader controlador = new FXMLLoader(MainApp.class.getResource("/fxml/aniadirPersona.fxml"));
			scene = new Scene(controlador.load());
			s.setTitle("Nueva ModeloPersona");
			s.setScene(scene);
			aniadirPersonaController controller = controlador.getController();
			controller.setTablaPersonas(tablaPersonas);
		} catch (IOException e) {
			e.printStackTrace();
		}
        s.setResizable(false);
        s.initOwner(MainApp.getStage());
        s.initModality(javafx.stage.Modality.WINDOW_MODAL);
        s.showAndWait();
        accionFiltrar(event);
        tablaPersonas.refresh();
    }
    
    /**
     * Eliminar persona.
     *
     * @param event the event
     */
    @FXML
    void eliminarPersona(ActionEvent event) {
    	Alert al=new Alert(AlertType.CONFIRMATION);
    	al.setHeaderText(null);
    	if(tablaPersonas.getSelectionModel().getSelectedItem()!=null) {
	    	al.setContentText("Estas seguro de que deseas borrar a: "+tablaPersonas.
	    			getSelectionModel().getSelectedItem());
	    	al.showAndWait();
	    	if(al.getResult().getButtonData().name().equals("OK_DONE")) {
	    		DaoPersona.eliminar(tablaPersonas.getSelectionModel().getSelectedItem());
	    		listaTodas=DaoPersona.cargarListaPersonas();
	    		accionFiltrar(event);
	    		tablaPersonas.refresh();
	    	}
    	}else {
    		al.setAlertType(AlertType.ERROR);
    		al.setContentText("No hay nadie seleccionado, asi que no se puede seleccionar nadie");
        	al.showAndWait();
    	}
    	tablaPersonas.getSelectionModel().clearSelection();
    }

    /**
     * Modificar persona.
     *
     * @param event the event
     */
    @FXML
    void modificarPersona(ActionEvent event) {
    	esAniadir=false;
    	if(tablaPersonas.getSelectionModel().getSelectedItem()!=null) {
	    	s=new Stage();
	    	Scene scene;
			try {
				 FXMLLoader controlador = new FXMLLoader(MainApp.class.getResource("/fxml/aniadirPersona.fxml"));
				scene = new Scene(controlador.load());
				s.setTitle("Modificar ModeloPersona");
				s.setScene(scene);
				aniadirPersonaController controller = controlador.getController();
				controller.setTxtApellidosText(tablaPersonas.getSelectionModel().
						getSelectedItem().getApellidos());
				controller.setTxtEdadText(tablaPersonas.getSelectionModel().
						getSelectedItem().getEdad()+"");
				controller.setTxtNombreText(tablaPersonas.getSelectionModel().
						getSelectedItem().getNombre());
				controller.setTablaPersonas(tablaPersonas);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        s.setResizable(false);
	        s.initOwner(MainApp.getStage());
	        s.initModality(javafx.stage.Modality.WINDOW_MODAL);
	        s.showAndWait();
	        accionFiltrar(event);
	        tablaPersonas.refresh();
    	}
    	else {
    		Alert al=new Alert(AlertType.ERROR);
        	al.setHeaderText(null);
        	al.setContentText("No hay nadie seleccionado, asi que no se puede seleccionar nadie");
        	al.showAndWait();
    	}
    }
    
    /**
     * Filtra en la lista para que solo se vean aquellas personas que su nombre contenga el texto introducido.
     *
     * @param event the event
     */
    @FXML
    void accionFiltrar(ActionEvent event) {
    	tablaPersonas.setItems(filtro);
    	if(txtFiltro.getText().isEmpty()){
    		tablaPersonas.setItems(listaTodas);
    	}else {
    		filtro.setPredicate(persona -> persona.getNombre().contains(txtFiltro.getText()));
    	}
    }
    /**
     * Si se pulsa el click derecho sobre un objeto de la tabla, sale un menu contextual que da la opcion de modificar y eliminar
     * 
     * @param event
     */
    @FXML
    void mostrarMenuContextual(MouseEvent event) {
    	if(event.getButton()==MouseButton.SECONDARY) {
    		contextMenu.show(MainApp.getStage());
    	}
    }
    
    /**
     * Inicializa el valor de las celdas.
     */
    @FXML
    private void initialize() {
    	idTablaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	idTablaApellido.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
    	idTablaEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
    	tablaPersonas.getItems().addAll(DaoPersona.cargarListaPersonas());
    	listaTodas=tablaPersonas.getItems();
    	filtro = new FilteredList<ModeloPersona>(listaTodas);
    	contextMenu = new ContextMenu();
    	MenuItem item1 = new MenuItem("Modificar");
        MenuItem item2 = new MenuItem("Eliminar");
        item1.setOnAction(event -> modificarPersona(event));
        item2.setOnAction(event -> eliminarPersona(event));
        contextMenu.getItems().addAll(item1,item2);
        FontIcon iconoAniadir = new FontIcon(FontAwesomeRegular.PLUS_SQUARE);
        iconoAniadir.setIconColor(javafx.scene.paint.Color.WHITE);
        btnAniadir.setGraphic(iconoAniadir);
        FontIcon iconoModificar=new FontIcon(FontAwesomeRegular.CARET_SQUARE_UP);
        iconoModificar.setIconColor(javafx.scene.paint.Color.WHITE);
        btnModificar.setGraphic(iconoModificar);
        FontIcon iconoEliminar=new FontIcon(FontAwesomeRegular.MINUS_SQUARE);
        iconoEliminar.setIconColor(javafx.scene.paint.Color.WHITE);
        btnEliminar.setGraphic(iconoEliminar);
    }

	/**
	 * Getter del stage.
	 *
	 * @return El stage
	 */
	public static Stage getS() {
		return s;
	}
	
	/**
	 * Getter de listaTodas.
	 *
	 * @return la lista de todas las persoans
	 */
	public static ObservableList<ModeloPersona> getListaTodas() {
		return listaTodas;
	}
	
	/**
	 * Setter de listaTodas.
	 *
	 * @param listaTodas la lista de todas las persoans
	 */
	public static void setListaTodas(ObservableList<ModeloPersona> listaTodas) {
		tablaPersonasController.listaTodas = listaTodas;
	}

	/**
	 * Verifica si esta añadiendo una persona o modificandola.
	 *
	 * @return true, si esta añadiendo una persona
	 */
	public static boolean isEsAniadir() {
		return esAniadir;
	}
    
}
