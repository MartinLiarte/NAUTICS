/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField tfPassword;

    /**
     * Initializes the controller class.
     */
    
    Navigation nav;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox VBoxCentro;
    @FXML
    private Label textoNautics;
    @FXML
    private VBox VBoxCampos;
    @FXML
    private HBox HBoxNombreUsuarioCorreo;
    @FXML
    private Label textoNombreUsuarioCorreo;
    @FXML
    private HBox HBoxContrasena;
    @FXML
    private Label textoContrasena;
    @FXML
    private Button btLogin;
    @FXML
    private Hyperlink linkCrearCuenta;
    @FXML
    private Label lbError;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbError.setText("");
        try {
            nav = Navigation.getInstance();
        } catch (NavDAOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
     private void showRegisterDialog() throws IOException {
        FXMLLoader miCargador = new
        FXMLLoader(getClass().getResource("register.fxml"));
        Parent root = miCargador.load();
        Scene scene = new Scene(root,900,600);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMinWidth(1224);
        stage.setMinHeight(768);
        stage.setTitle("Ver datos persona");
        stage.initModality(Modality.APPLICATION_MODAL);
        //la ventana se muestra modal
        stage.setMinWidth(1224);
        stage.setMinHeight( 768);
        stage.showAndWait();

    }

    @FXML
    private void registerAction(ActionEvent event) throws IOException {
        showRegisterDialog();
    }

    @FXML
    private void login(ActionEvent event){
        boolean existeUser = false;
        boolean correctPassword = false;
        boolean coorectCredentials = false;
        
        String nick = tfUser.getText();
        String password = tfPassword.getText();
        
        existeUser = nav.exitsNickName(nick);
        
        if(existeUser==false){
            lbError.setText("Usuario no registrado");
            return;
        }
        
        User miUser = nav.authenticate(nick, password);
        
        if(miUser==null){
            lbError.setText("Contraseña incorrecta");
            return;
        }
        
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("inicio.fxml"));
        Parent root = loader.load();

        // Obtener el controlador para pasar datos
        InicioController inicioController = loader.getController();

        // Crear sesión, hits y fails de ejemplo o los que necesites
        //Session sesion = new Session(LocalDateTime.now(), 0, 0);
        int hits = 0;
        int fails = 0;

        // Obtener la ventana actual desde el evento
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        
        // Pasar los datos al controlador de inicio
        inicioController.ini(miUser, hits, fails,stage);

        // Cambiar la escena
        Scene scene = new Scene(root);
        double width = stage.getWidth();
        double height = stage.getHeight();
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle("Inicio");
        stage.show();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }




    
}
