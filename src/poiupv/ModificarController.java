/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

/**
 * FXML Controller class
 *
 */
public class ModificarController implements Initializable {

    Navigation nav;
    
    User user;
    
    
    @FXML
    private TextField tfUser;
    @FXML
    private Label lbUserError;
    @FXML
    private TextField tfPassword;
    @FXML
    private Label lbPasswordError;
    @FXML
    private TextField tfMail;
    @FXML
    private Label lbMailError;
    @FXML
    private ImageView avatar;
    @FXML
    private VBox vBox1;
    @FXML
    private Text textoCrearCuenta;
    @FXML
    private TextField tfRepeat;
    @FXML
    private Label lbRepeatError;
    @FXML
    private DatePicker datepicker;
    @FXML
    private Label lbDateError;
    @FXML
    private Button btRegister;
    
    /**
     * Initializes the controller class.
     */
    
    public void ini(User user){
        if(user==null){
            System.out.println("User null");
            return;
        }
        this.user=user;
        
        if(user.getAvatar() != null) {
        avatar.setImage(user.getAvatar());
        }
        
        tfUser.setText(user.getNickName());
        tfPassword.setText(user.getPassword());
        tfRepeat.setText(user.getPassword());
        tfMail.setText(user.getEmail());
        datepicker.setValue(user.getBirthdate());
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ocultar etiquetas error
        
        tfUser.setEditable(false);
        
        lbUserError.setVisible(false);
        lbPasswordError.setVisible(false);
        lbRepeatError.setVisible(false);
        lbMailError.setVisible(false);
        lbDateError.setVisible(false);
        
        //Inicializamos Navigation
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
    private void registerAction(ActionEvent event) throws NavDAOException {
        
        boolean ok = true;
        String nick = user.getNickName();
        String mail = tfMail.getText();
        String pssw = tfPassword.getText();
        LocalDate date = datepicker.getValue();
        Image img = avatar.getImage();
        
        lbPasswordError.setVisible(!checkPassword(pssw));
        lbMailError.setVisible(!checkMail(mail));
        lbDateError.setVisible(!checkDate(date));
        
        ok=ok&&checkPassword(pssw)&&checkMail(mail)&&checkDate(date);
        if(ok==false){
            System.out.println("No registrado");
            return;
        }
        
        user.setPassword(pssw);
        user.setEmail(mail);
        user.setAvatar(img);
        user.setBirthdate(date);
        System.out.println("Si registrado");
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }
    
}
