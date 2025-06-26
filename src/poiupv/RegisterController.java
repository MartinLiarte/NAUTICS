/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import com.sun.jdi.connect.spi.Connection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.Session;
import model.User;
import static model.User.checkNickName;
import model.sub.SqliteConnection;
import static model.sub.SqliteConnection.connectSqlite;

//import static model.Navigation.registerUser;

/**
 * FXML Controller class
 *
 * @author Stanie
 */
public class RegisterController implements Initializable {

    @FXML
    private Text textoCrearCuenta;
    @FXML
    private TextField tfUser;
    @FXML
    private Label lbUserError;
    @FXML
    private TextField tfPassword;
    @FXML
    private Label lbPasswordError;
    @FXML
    private TextField tfRepeat;
    @FXML
    private Label lbRepeatError;
    @FXML
    private TextField tfMail;
    @FXML
    private Label lbMailError;
    @FXML
    private DatePicker datepicker;
    @FXML
    private Label lbDateError;
    @FXML
    private ImageView avatar;
    @FXML
    private Button btRegister;
    @FXML
    private VBox vBox1;

    Navigation nav;
    
    //SQLConnection con;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ocultar etiquetas error
        lbUserError.setVisible(false);
        lbPasswordError.setVisible(false);
        lbRepeatError.setVisible(false);
        lbMailError.setVisible(false);
        lbDateError.setVisible(false);
        datepicker.setValue(LocalDate.of(2005, 1, 1));
        //Inicializamos Navigation
        /*
        SqliteConnection sqlite = new SqliteConnection();
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
        
        
        
        

    }    

    @FXML
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
        alert.setTitle("");
        alert.setHeaderText("Selecciona un archivo");
        alert.setContentText("");
        alert.showAndWait();
    }
    }
    
    private boolean checkMail(String mail){
        String email = mail;
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return isValid;
    }
    
    
    
    private boolean checkPassword(String pssw) {
        String password = pssw;
        boolean isValid = password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,15}$");
        return isValid;
    }
    
    private boolean checkDate(LocalDate date){
        LocalDate value = date;
        boolean isValid = value.isBefore(LocalDate.now().minus(18, YEARS));
        return isValid;
    }

    @FXML
    private void registerAction(ActionEvent event) throws NavDAOException, IOException {
        
        boolean ok = true;
        String nick = tfUser.getText();
        String mail = tfMail.getText();
        String pssw = tfPassword.getText();
        String rept = tfRepeat.getText();
        LocalDate date = datepicker.getValue();
        Image img = avatar.getImage();
        
        lbUserError.setVisible(!checkNickName(nick));
        lbPasswordError.setVisible(!checkPassword(pssw));
        lbRepeatError.setVisible(!pssw.equals(rept));
        lbMailError.setVisible(!checkMail(mail));
        lbDateError.setVisible(!checkDate(date));
        
        ok=ok&&checkNickName(nick)&&checkPassword(pssw)&&pssw.equals(rept)&&checkMail(mail)&&checkDate(date);
        if(ok==false){
            System.out.println("No registrado");
            return;
        }
        
        nav.registerUser(nick,mail,pssw,img,date);
        System.out.println("Si registrado");
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
        
          
        
    }


    
    
    
}