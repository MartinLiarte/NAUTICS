/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import model.Session;
import model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import poiupv.InicioController;

public class ResultadosController {

    private User usuario;

    //private Session sesion;

    private int hits;

    private int fails;
    
    Stage myStage;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label labelAllTime;

    @FXML
    private Label labelPeriodo;

    @FXML
    private Button btnInicio;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void ini(User user, int h, int f,Stage stg) {
        if (user == null) {
            System.out.println("User null");
            return;
        }
        this.usuario = user;
        this.hits = h;
        this.fails = f;
        actualizarPorcentajes();  // Muestro ambos porcentajes al cargar usuario
        myStage=stg;
        myStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Solicitud de cierre de ventana detectada.RESULTADOS");
            System.out.println("Guardando sesión...");
            user.addSession(hits, fails);
            System.out.println("Sesión guardada");

            // Consume el evento para evitar que la ventana se cierre inmediatamente
            // hasta que el usuario confirme o cancele.
            event.consume();
            Platform.exit();
        });
    }
    @FXML
    private void initialize() {
        // Siempre muestra total al inicio
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                actualizarPorcentajeFiltrado(newDate);
            } else {
                labelPeriodo.setText("");
            }
        });
    }

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

        stage.setScene(new Scene(root));
        stage.setMinWidth(1024);
        stage.setMinHeight( 768);
        stage.setTitle("Pantalla de Inicio");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void actualizarPorcentajes() {
        // Porcentaje total
        int totalHits = 0;
        int totalFaults = 0;

        List<Session> sesiones = usuario.getSessions();

        for (Session s : sesiones) {
            totalHits += s.getHits();
            totalFaults += s.getFaults();
        }

        double porcentajeTotal = calcularPorcentaje(totalHits, totalFaults);
        labelAllTime.setText(String.format("Tasa de aciertos total: %.2f%%", porcentajeTotal));
        labelPeriodo.setText("");  // Sin filtro inicial
    }

    private void actualizarPorcentajeFiltrado(LocalDate fechaFiltro) {
        // Siempre mostramos el total en labelAllTime
        actualizarPorcentajes();

        // Ahora el filtrado para labelPeriodo
        List<Session> sesionesFiltradas = usuario.getSessions().stream()
                .filter(s -> !s.getTimeStamp().toLocalDate().isBefore(fechaFiltro))
                .collect(Collectors.toList());

        int hitsFiltrados = 0;
        int faultsFiltrados = 0;

        for (Session s : sesionesFiltradas) {
            hitsFiltrados += s.getHits();
            faultsFiltrados += s.getFaults();
        }

        double porcentajeFiltrado = calcularPorcentaje(hitsFiltrados, faultsFiltrados);

        labelPeriodo.setText(String.format("Tasa de aciertos desde la fecha %s: %.2f%%",
                fechaFiltro.format(formatter), porcentajeFiltrado));
    }

    private double calcularPorcentaje(int hits, int faults) {
        int total = hits + faults;
        if (total == 0) return 0.0;
        return (hits * 100.0) / total;
    }}

