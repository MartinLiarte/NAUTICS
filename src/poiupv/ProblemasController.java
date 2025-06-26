/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.IOException;
import static java.lang.Math.random;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Problem;
import model.Navigation;
import model.User;
import model.NavDAOException;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.collections.ObservableList;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import model.Session;


/**
 *
 */
public class ProblemasController implements Initializable{

    @FXML
    private Button btnInicio;
    @FXML
    private TextField barraBusqueda;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button botonAleatorio;
    @FXML
    private ListView<Problem> listaView;
    
    private User usuario;
     
    //Session sesion;
    
    int hits;
    
    int fails;
    
    Stage myStage;
    @FXML
    private HBox imageContainer;
    @FXML
    private HBox hBoxLista;
    @FXML
    private HBox hBoxPadre;
    
    public void ini(User user,int h,int f,Stage stg){
        if(user==null){
            System.out.println("User null");
            return;
        }
        this.usuario=user;
        hits = h;
        fails = f;
        myStage=stg;
        myStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Solicitud de cierre de ventana detectada.PROBLEMAS");
            System.out.println("Guardando sesión...");
            user.addSession(hits, fails);
            System.out.println("Sesión guardada");

            // Consume el evento para evitar que la ventana se cierre inmediatamente
            // hasta que el usuario confirme o cancele.
            event.consume();
            Platform.exit();
        });
    }
    

    // Para sacar uno al azar
    private final Random rnd = new Random();
    private ObservableList<Problem> allProblems = FXCollections.observableArrayList();

    /**
     * Se ejecuta tras cargar el FXML.
     * Carga todos los problemas desde SQLite y los muestra en el ListView.
     */
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Problem> datos = Navigation.getInstance().getProblems();
            allProblems.setAll(datos);
            listaView.setItems(allProblems);

            // Mostrar solo el texto de cada problema en la lista
            listaView.setCellFactory(lv -> new ListCell<Problem>() {
                @Override
                protected void updateItem(Problem item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty || item == null) ? null : item.getText());
                }
            });

        } catch (NavDAOException e) {
            e.printStackTrace();
        }
        listaView.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) {  // doble click para seleccionar
            Problem seleccionado = listaView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("carta.fxml"));
                    Parent root = loader.load();

                    CartaController controller = loader.getController();
                    
                    Stage stage = (Stage) listaView.getScene().getWindow(); // ventana actual
                    controller.setProblema(seleccionado,usuario,hits,fails,stage);  // pasar problema seleccionado

                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalle del Problema");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
        hBoxLista.scaleXProperty().bind(hBoxPadre.scaleXProperty());
    }

    /**
     * Acción para el botón "Aleatorio" (fx:id="botonAleatorio").
     * Toma un problema al azar de la lista y muestra su carta.
     */
    @FXML
    private void aleatorio(ActionEvent event) {
        List<Problem> items = listaView.getItems();
        if (items.isEmpty()) {
            System.err.println("No hay problemas disponibles.");
            return;
        }

        // Elegimos uno al azar
        Problem seleccionado = items.get(rnd.nextInt(items.size()));
        //seleccionado.setEstado("empezado");

        try {
            // Cargar la vista de carta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("carta.fxml"));
            Parent root = loader.load();

            // Pasar el problema al controlador de carta
            CartaController cartaCtrl = loader.getController();
            //cartaCtrl.setProblema(seleccionado);
            
            // Reemplazar la escena actual
            Stage ventana = (Stage) botonAleatorio.getScene().getWindow();
            cartaCtrl.setProblema(seleccionado,usuario,hits,fails,ventana);

            ventana.setScene(new Scene(root));
            ventana.setTitle("Carta Náutica del Problema");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Aquí puedes añadir más handlers, p.ej. filtrar, buscar, volver a inicio, etc.

    @FXML
private void inicio(ActionEvent event) {
    try {
        // 1) Carga el FXML de inicio
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/poiupv/inicio.fxml")
        );
        Parent root = loader.load();

        // 2) Le pasamos el User logueado al InicioController
        InicioController inicioCtrl = loader.getController();
       
        // 3) Cambiamos la escena
        Stage stage = (Stage) btnInicio.getScene().getWindow();
        inicioCtrl.ini(this.usuario,hits,fails,stage);    //TE CAMBIO ESTO MARTIN, NECESITO USER,SESSION,HITS,FAILS

        stage.setMinWidth(1024);
        stage.setMinHeight( 768);
        stage.setScene(new Scene(root));
        stage.setTitle("Pantalla de Inicio");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void buscar(ActionEvent event) {
        String term = barraBusqueda.getText().trim().toLowerCase();
        if (term.isEmpty()) {
            // si vacía, restauramos toda la lista
            listaView.setItems(allProblems);
        } else {
            // filtramos por enunciado que contenga el término
            ObservableList<Problem> filtered = allProblems.stream()
                .filter(p -> p.getText().toLowerCase().contains(term))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
            listaView.setItems(filtered);
        }
    }
}