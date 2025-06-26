/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;
import model.sub.SqliteConnection;

/**
 * FXML Controller class
 *
 */

public class InicioController implements Initializable {


    Navigation nav;
    
    User user;
    
    //Session sesion;
    
    int hits;
    
    int fails;
    
    Stage myStage;
    @FXML
    private Button btHacerProblemas;
    @FXML
    private Button btVerResultados;
    @FXML
    private ImageView avatar;
    @FXML
    private Button btModificar;
    @FXML
    private Button btLogOut;
    @FXML
    private ImageView salir;
    @FXML
    private HBox hBoxMenu;
    @FXML
    private VBox vBox;
    
    public void ini(User user,int h,int f,Stage stg){
        if(user==null){
            System.out.println("User null");
            return;
        }
        this.user=user;
        
            hits = h;
            fails = f;
            System.out.println("Hits:"+hits+"   Fails:"+fails);
        
        
        if(user.getAvatar() != null) {
        avatar.setImage(user.getAvatar());
        }
        myStage=stg;
        myStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Solicitud de cierre de ventana detectada.INICIO");
            System.out.println("Guardando sesión...");
            user.addSession(hits, fails);
            System.out.println("Sesión guardada");

            // Consume el evento para evitar que la ventana se cierre inmediatamente
            // hasta que el usuario confirme o cancele.
            event.consume();
            Platform.exit();
        });
    }
    
    public void stop() {
           System.out.println("Aplicación cerrándose, realizando tareas de limpieza...");
           // Guardar datos, liberar recursos, etc.
           user.addSession(hits, fails);
           Platform.exit(); // Finaliza la aplicación
       }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //SqliteConnection sqlite = new SqliteConnection();
        /*
        try {
            sqlite.connectSqlite("D:\\uni\\segundo\\IPC\\PROYECTO - copia\\NAUTICS-master\\data.db");
            System.out.println("Base de datos conectada");
        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        try {
            nav = Navigation.getInstance();
        } catch (NavDAOException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        AnchorPane.setTopAnchor(hBoxMenu,0.0);
        AnchorPane.setRightAnchor(hBoxMenu,0.0);
    }    
    
    private void cambiarFoto(MouseEvent event) throws FileNotFoundException {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Open File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.bmp", "*.png", "*.jpg", "*.gif")); 
    File file = chooser.showOpenDialog(new Stage());
    

    if(file != null) {
            //String imagepath = file.toURI().toURL().toString();
            InputStream isImage = (InputStream) new FileInputStream(file);
            avatar.setImage(new Image(isImage));
    }
    else
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Please Select a File");
        alert.setContentText("You didn't select a file!");
        alert.showAndWait();
    }
    }
    

    @FXML
    private void hacerProblemas(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("problemas.fxml"));
        Parent root = loader.load();

        // Obtener el controlador para pasar datos
        ProblemasController problemasController = loader.getController();

        // Crear sesión, hits y fails de ejemplo o los que necesites
        //Session sesion = new Session(LocalDateTime.now(), 0, 0);

        // Pasar los datos al controlador de inicio
        

        // Obtener la ventana actual desde el evento
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        problemasController.ini(user,hits,fails,stage);
        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Elige un problema");
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }

    @FXML
    private void verResultados(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resultados.fxml"));
        Parent root = loader.load();

        // Obtener el controlador para pasar datos
        ResultadosController resultadosController = loader.getController();

        // Crear sesión, hits y fails de ejemplo o los que necesites
        //Session sesion = new Session(LocalDateTime.now(), 0, 0);

        // Pasar los datos al controlador de inicio

        // Obtener la ventana actual desde el evento
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        resultadosController.ini(user,hits,fails,stage);
        // Cambiar la escena
        Scene scene = new Scene(root);
        double width = stage.getWidth();
        double height = stage.getHeight();
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle("Elige un problema");
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }

    @FXML
    private void modificarPerfil(ActionEvent event) throws IOException {
        showModificarDialog();
    }
    
     private void showModificarDialog() throws IOException {
        FXMLLoader miCargador = new
        FXMLLoader(getClass().getResource("modificar.fxml"));
        Parent root = miCargador.load();
        ModificarController modificarController = miCargador.getController();   // Obtener el controlador para pasar parámetros
        modificarController.ini(user);
        Scene scene = new Scene(root,900,600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Modificar tu perfil");
        stage.setMinWidth(1224);
        stage.setMinHeight(768);
        stage.initModality(Modality.APPLICATION_MODAL);
        //la ventana se muestra modal
        stage.showAndWait();
        avatar.setImage(user.getAvatar());

    }

    @FXML
    private void logout(ActionEvent event) {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Obtener el controlador para pasar datos
        LoginController loginController = loader.getController();

        // Crear sesión, hits y fails de ejemplo o los que necesites
        //Session sesion = new Session(LocalDateTime.now(), 0, 0);
        user.addSession(hits, fails);

        // Pasar los datos al controlador de inicio

        // Obtener la ventana actual desde el evento
        //MenuItem source = (MenuItem) event.getSource();
        Stage stage = (Stage) btLogOut.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inicio de sesión");
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    
}
