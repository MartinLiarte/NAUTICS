/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Answer;
import model.Problem;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 */
public class CartaController implements Initializable {
    
    private User usuario;
     
    Session sesion;
    
    int hits;
    
    int fails;
    
    Stage myStage;

    
    private Group zoomGroup;
    private Group lineaYCirculosGroup;
    private ImageView regla;
    private ImageView transportador;
    private Line linePainting;
    private Arc circlePainting;
    
    
    //transportador
    private double baseXTransportador, baseYTransportador;
    private Point2D localBase;
    
    //regla
    private Point2D localBaseRegla;
    private double baseXRegla, baseYRegla;
    
    //arco
    private double inicioXArc,inicioYArc;
    private boolean radioCambiable = true;
    private double radioArco = 1.0;
    
    private double grosorGeneral = 1.0;
    
    
    private final ToggleGroup respuestasGroup = new ToggleGroup();
    private Problem problema;
    
    
    private double scrollHInicio;
    private double scrollVInicio;
    private double mouseXInicio;
    private double mouseYInicio;
    
    //Circulos de la linea
    private Circle circuloInicio;
    private Circle circuloFin;

    @FXML
    private Label lblEnunciado;
    @FXML
    private Button btnEnviar;
    @FXML
    private Button btnInicio;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private HBox hBoxCentro;
    @FXML
    private HBox hBoxCartaHerramientas;
    @FXML
    private VBox vBoxHerramientas;
    @FXML
    private VBox vBoxRespuestas;
    @FXML
    private ImageView imageViewCarta;
    @FXML
    private ScrollPane scrollCarta;
    @FXML
    private Slider zoom_slider;
    @FXML
    private ToggleButton botonRegla;
    @FXML
    private ToggleButton botonTransportador;
    @FXML
    private ColorPicker btnColor;
    @FXML
    private ToggleButton botonPunto;
    @FXML
    private ToggleButton btnLinea;
    @FXML
    private ToggleButton btnArco;
    @FXML
    private ToggleButton btnText;
    @FXML
    private ToggleButton tbResp1;
    @FXML
    private ToggleButton tbResp2;
    @FXML
    private ToggleButton tbResp3;
    @FXML
    private ToggleButton tbResp4;
    @FXML
    private ToggleButton btnZoomIn;
    @FXML
    private ToggleButton btnZoomOut;
    @FXML
    private ToggleButton btnBorrarTodo;
    @FXML
    private ToggleGroup herramientasGroup;
    @FXML
    private HBox hBoxEnunciado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        herramientasGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {  
            ToggleButton selectedButton = (ToggleButton) newValue;
            selectedButton.setStyle("-fx-background-color: #6c00fe;");
                if (oldValue != null) {
                    ToggleButton previousButton = (ToggleButton) oldValue;
                    previousButton.setStyle("-fx-background-color: #0B1320;");
                    oldValue.setSelected(false);   
                }
        }
        });
        //BOTONES DISEÑO
        btnZoomIn.setOnMousePressed(e -> {
            btnZoomIn.setStyle("-fx-background-color: #6c00fe;"); 
        });

        btnZoomIn.setOnMouseReleased(e -> {
            btnZoomIn.setStyle("-fx-background-color: #0B1320;"); 
            double newZoom = zoom_slider.getValue() + 0.1;
            zoom_slider.setValue(Math.min(newZoom, zoom_slider.getMax())); 
        });
        
        Tooltip tooltipZoomIn = new Tooltip("Aumenta el zoom del mapa");
        btnZoomIn.setTooltip(tooltipZoomIn);

      
        btnZoomOut.setOnMousePressed(e -> {
            btnZoomOut.setStyle("-fx-background-color: #6c00fe;"); 
        });

        btnZoomOut.setOnMouseReleased(e -> {
            btnZoomOut.setStyle("-fx-background-color: #0B1320;"); 
            double newZoom = zoom_slider.getValue() - 0.1;
            zoom_slider.setValue(Math.max(newZoom, zoom_slider.getMin())); 
        });
        
        Tooltip tooltipZoomOut = new Tooltip("Disminuye el zoom del mapa");
        btnZoomOut.setTooltip(tooltipZoomOut);
       
        Tooltip tooltipColor = new Tooltip("Cambia de color la herramienta seleccionada");
        btnColor.setTooltip(tooltipColor);
        
        botonPunto.setOnAction(e -> {
        if (botonPunto.isSelected()) {
            botonPunto.setStyle("-fx-background-color: #6c00fe;");
        } else {
            botonPunto.setStyle("-fx-background-color: #0B1320;");
                }
        });
       
        Tooltip tooltipPunto = new Tooltip("Marca una X en el mapa\nNOTA: Con clic derecho puedes eliminarlo");
        botonPunto.setTooltip(tooltipPunto);
        
        btnLinea.setOnAction(e -> {
        if (btnLinea.isSelected()) {
            btnLinea.setStyle("-fx-background-color: #6c00fe;");
            mostrarDialogoLinea(null);
        } else {
            btnLinea.setStyle("-fx-background-color: #0B1320;");
                }
        });
       
        Tooltip tooltipLinea = new Tooltip("Dibuja una linea\nNOTA: Con clic derecho puedes eliminarlo");
        btnLinea.setTooltip(tooltipLinea);
        
        btnArco.setOnAction(e -> {
        if (btnArco.isSelected()) {
            btnArco.setStyle("-fx-background-color: #6c00fe;");
            mostrarDialogoArco(null);
        } else {
            btnArco.setStyle("-fx-background-color: #0B1320;");
                }
        });
       
        Tooltip tooltipArco = new Tooltip("Traza un arco\nNOTA: Con clic derecho puedes eliminarlo");
        btnArco.setTooltip(tooltipArco);
        
        btnText.setOnAction(e -> {
        if (btnText.isSelected()) {
            btnText.setStyle("-fx-background-color: #6c00fe;");
        } else {
            btnText.setStyle("-fx-background-color: #0B1320;");
                }
        });
       
        Tooltip tooltipTexto = new Tooltip("Añade texto\nNOTA: Con clic derecho puedes eliminarlo");
        btnText.setTooltip(tooltipTexto);
        
        botonRegla.setOnAction(e -> {
        if (botonRegla.isSelected()) {
            botonRegla.setStyle("-fx-background-color: #6c00fe;");
            onRegla();
            
        } else {
            botonRegla.setStyle("-fx-background-color: #0B1320;");
            regla.setVisible(false); 
                }
        });
       
        Tooltip tooltipRegla = new Tooltip("Se inserta una regla sobre el mapa");
        botonRegla.setTooltip(tooltipRegla);
        
        botonTransportador.setOnAction(e -> {
        if (botonTransportador.isSelected()) {
            botonTransportador.setStyle("-fx-background-color: #6c00fe;");
            transportador.setVisible(true);
        } else {
            botonTransportador.setStyle("-fx-background-color: #0B1320;");
            transportador.setVisible(false);
                }
        });
       
        Tooltip tooltipTransportador = new Tooltip("Se inserta un transportador sobre el mapa");
        botonTransportador.setTooltip(tooltipTransportador);
        
        
        
        btnBorrarTodo.setOnMousePressed(e -> {
            btnBorrarTodo.setStyle("-fx-background-color: #6c00fe;");
            borrarTodo();
        });

        btnBorrarTodo.setOnMouseReleased(e -> {
            btnBorrarTodo.setStyle("-fx-background-color: #0B1320;");
        });
       
        Tooltip tooltipBorrarTodo = new Tooltip("Se borra todos los elementos que hay sobre el mapa");
        btnBorrarTodo.setTooltip(tooltipBorrarTodo);


        //RESCALADO DE LA CARTA Y SUS ELEMENTOS
        VBox.setVgrow(vBoxHerramientas, Priority.ALWAYS);
        HBox.setHgrow(hBoxCentro, Priority.ALWAYS);
        imageViewCarta.fitWidthProperty().bind(hBoxCentro.widthProperty());
        imageViewCarta.fitHeightProperty().bind(hBoxCentro.heightProperty());
        scrollCarta.setFitToHeight(true);
        lblEnunciado.setWrapText(true);
        HBox.setHgrow(lblEnunciado, Priority.ALWAYS);
        lblEnunciado.setMaxWidth(Double.MAX_VALUE);

        
        //SOBRE EL ZOOM
        zoom_slider.setMin(1.0);
        zoom_slider.setMax(6.0);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scaleValue = newValue.doubleValue();
            zoom(scaleValue);  
        });
        
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(scrollCarta.getContent());
        scrollCarta.setContent(contentGroup);
        
        Pane herramientaPane = new Pane();
        herramientaPane.setManaged(false);
        anchorPane.getChildren().add(herramientaPane);
        
        //MOVER LA CARTA CON EL CLIC
        scrollCarta.setPannable(false);  // desactivar pannable por defecto

        scrollCarta.getContent().setOnMousePressed(event -> {
            mouseXInicio = event.getX();
            mouseYInicio = event.getY();
            scrollHInicio = scrollCarta.getHvalue();
            scrollVInicio = scrollCarta.getVvalue();
        });

        scrollCarta.getContent().setOnMouseDragged(event -> {
            double deltaX = mouseXInicio - event.getX();
            double deltaY = mouseYInicio - event.getY();

            double ancho = scrollCarta.getContent().getBoundsInLocal().getWidth();
            double altura = scrollCarta.getContent().getBoundsInLocal().getHeight();

            double viewportAncho = scrollCarta.getViewportBounds().getWidth();
            double viewportAltura = scrollCarta.getViewportBounds().getHeight();

            double hDelta = deltaX / (ancho - viewportAncho);
            double vDelta = deltaY / (altura - viewportAltura);

            //Esto es para que no vaya al infinito
            scrollCarta.setHvalue(Math.min(Math.max(scrollHInicio + hDelta, 0), 1));
            scrollCarta.setVvalue(Math.min(Math.max(scrollVInicio + vDelta, 0), 1));
        });
        
        //SOBRE LA REGLA
        regla = new ImageView(new Image(getClass().getResourceAsStream("/resources/regla.png")));
        regla.setOpacity(0.8);
        regla.setVisible(false); 
        regla.setPreserveRatio(true); 
        regla.setFitWidth(600);
        regla.setPickOnBounds(false);
        herramientaPane.getChildren().add(regla);
        
        regla.setLayoutX(70); 
        regla.setLayoutY(100);
        
        regla.setOnMousePressed(event -> {
            localBaseRegla = herramientaPane.sceneToLocal(event.getSceneX(), event.getSceneY());
            baseXRegla = regla.getLayoutX();
            baseYRegla = regla.getLayoutY();
            event.consume();
        });

        regla.setOnMouseDragged(event -> {
            Point2D localPos = herramientaPane.sceneToLocal(event.getSceneX(), event.getSceneY());

            double deltaX = localPos.getX() - localBaseRegla.getX();
            double deltaY = localPos.getY() - localBaseRegla.getY();

            double nuevoX = baseXRegla + deltaX;
            double nuevoY = baseYRegla + deltaY;

            double anchoMax = imageViewCarta.getBoundsInParent().getWidth() - regla.getBoundsInParent().getWidth();
            double altoMax = imageViewCarta.getBoundsInParent().getHeight() - regla.getBoundsInParent().getHeight();

            nuevoX = Math.max(0, Math.min(nuevoX, anchoMax));
            nuevoY = Math.max(0, Math.min(nuevoY, altoMax));

            regla.setLayoutX(nuevoX);
            regla.setLayoutY(nuevoY);

            event.consume();
        });
        
        //SOBRE EL TRANSPORTADOR
        transportador = new ImageView(new Image(getClass().getResourceAsStream("/resources/transportador.png")));
        transportador.setOpacity(0.5);
        transportador.setVisible(false); 
        transportador.setPreserveRatio(true);
        transportador.setFitWidth(250);
        transportador.setPickOnBounds(false);
        herramientaPane.getChildren().add(transportador); 

        transportador.setLayoutX(70); 
        transportador.setLayoutY(100);
        
        herramientaPane.setPickOnBounds(false);
        
        transportador.setOnMousePressed(event -> {
            localBase = herramientaPane.sceneToLocal(event.getSceneX(), event.getSceneY());
            baseXTransportador = transportador.getLayoutX();
            baseYTransportador = transportador.getLayoutY();
            event.consume();
        });
        
        regla.setOnMouseReleased(event -> {
            regla.toBack(); 
            event.consume();
        });
        
        transportador.setOnMouseDragged(event -> {
            Point2D localPos = herramientaPane.sceneToLocal(event.getSceneX(), event.getSceneY());

            double deltaX = localPos.getX() - localBase.getX();
            double deltaY = localPos.getY() - localBase.getY();

            double nuevoX = baseXTransportador + deltaX;
            double nuevoY = baseYTransportador + deltaY;

            double anchoMax = imageViewCarta.getBoundsInParent().getWidth() - transportador.getBoundsInParent().getWidth();
            double altoMax = imageViewCarta.getBoundsInParent().getHeight() - transportador.getBoundsInParent().getHeight();

            nuevoX = Math.max(0, Math.min(nuevoX, anchoMax));
            nuevoY = Math.max(0, Math.min(nuevoY, altoMax));

            transportador.setLayoutX(nuevoX);
            transportador.setLayoutY(nuevoY);

            event.consume();
        });
        
        transportador.setOnMouseReleased(event -> {
            transportador.toBack();  
            event.consume();
        });
        
        //DIBUJAR LA LINEA
        btnLinea.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                imageViewCarta.setOnMousePressed(this::presionarLinea);
                imageViewCarta.setOnMouseDragged(this::arrastrarLinea);
            
            } else {
         
                imageViewCarta.setOnMousePressed(null);
                imageViewCarta.setOnMouseDragged(null);
          
            }
        });
        
        if (!btnLinea.isSelected()) {
            // Esto hace que al iniciar la carta no traze la linea al clickar el mapa si no esta seleccionado su correspondiente boton
            imageViewCarta.setOnMousePressed(null);
            imageViewCarta.setOnMouseDragged(null);
            imageViewCarta.setOnMouseReleased(null);
        }
        
        //TRAZAR EL ARCO
        btnArco.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                imageViewCarta.setOnMousePressed(this::presionarArco);
                imageViewCarta.setOnMouseDragged(this::arrastrarArco);

            } else {
                imageViewCarta.setOnMousePressed(null);
                imageViewCarta.setOnMouseDragged(null);

            }
        });
        
        //ANADIR TEXTO
        btnText.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                imageViewCarta.setOnMousePressed(this::mostrarDialogoTexto);
            } else {
                imageViewCarta.setOnMousePressed(null);
            }
         });
        
        //ANADIR PUNTO
        botonPunto.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                imageViewCarta.setOnMouseClicked(this::marcarPunto);
            } else {
                imageViewCarta.setOnMouseClicked(null);
            }
        });
    
    }    

    @FXML
    private void zoomIn(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
        
    }

    @FXML
    private void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    private void zoom(double scaleValue) {
        double scrollH = scrollCarta.getHvalue();
        double scrollV = scrollCarta.getVvalue();
        
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        
        scrollCarta.setHvalue(scrollH);
        scrollCarta.setVvalue(scrollV);
    }

    
    private void onRegla() {
         if (botonRegla.isSelected()) {
             
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Configuración de la Regla");
            dialog.setHeaderText("Selecciona los grados de rotación de la regla");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            Slider rotacionSlider = new Slider(0, 360, 0);  
            rotacionSlider.setBlockIncrement(5); 
            rotacionSlider.setShowTickMarks(true);
            rotacionSlider.setShowTickLabels(true);
            rotacionSlider.setMajorTickUnit(90);  
            rotacionSlider.setMinorTickCount(4); 

            Label gradosLabel = new Label("Grados: 0");

            rotacionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                gradosLabel.setText("Grados: " + (int) newValue.intValue());
            });

            grid.add(new Label("Rotación:"), 0, 0);
            grid.add(rotacionSlider, 1, 0);
            grid.add(gradosLabel, 1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                double grados = rotacionSlider.getValue();
                rotarRegla(grados);
                regla.setVisible(true); 
            } else {
                botonRegla.setSelected(false);
                regla.setVisible(false);  
                botonRegla.setStyle("-fx-background-color: #0B1320;");  

            }
        }
        
    }
    
    private void rotarRegla(double grados) {
        regla.setRotate(grados);

        double anchoMax = imageViewCarta.getBoundsInParent().getWidth() - regla.getBoundsInParent().getWidth();
        double altoMax = imageViewCarta.getBoundsInParent().getHeight() - regla.getBoundsInParent().getHeight();

        double nuevoX = Math.max(0, Math.min(regla.getTranslateX(), anchoMax));
        double nuevoY = Math.max(0, Math.min(regla.getTranslateY(), altoMax));

        regla.setTranslateX(nuevoX);
        regla.setTranslateY(nuevoY);
    }
    
   
    
    public void presionarLinea(MouseEvent event) {
        circuloInicio = new Circle(event.getX(), event.getY(), 2); 
        circuloInicio.setFill(btnColor.getValue()); 

        circuloFin = new Circle(event.getX(), event.getY(), 2); 
        circuloFin.setFill(btnColor.getValue()); 

        lineaYCirculosGroup = new Group();
        
        linePainting = new Line(event.getX(), event.getY(), event.getX(), event.getY());
        linePainting.setStroke(btnColor.getValue());
        linePainting.setStrokeWidth(grosorGeneral);
        
        lineaYCirculosGroup.getChildren().addAll(circuloInicio, circuloFin, linePainting);
        zoomGroup.getChildren().add(lineaYCirculosGroup);
        
        circuloInicio.setCenterX(event.getX());
        circuloInicio.setCenterY(event.getY());
        
        linePainting.setOnContextMenuRequested(e -> {
            ContextMenu menuContext = new ContextMenu();
            MenuItem borrarItem = new MenuItem("Eliminar");
            menuContext.getItems().add(borrarItem);
            borrarItem.setOnAction(ev -> {
                zoomGroup.getChildren().remove(lineaYCirculosGroup);
                ev.consume();
            });

            menuContext.show(linePainting, e.getScreenX(), e.getScreenY());
            e.consume();
        });

    }
    
    public void arrastrarLinea(MouseEvent event) {
        if (linePainting != null && circuloInicio != null && circuloFin != null) {
            circuloFin.setCenterX(event.getX());
            circuloFin.setCenterY(event.getY());
            linePainting.setEndX(event.getX());
            linePainting.setEndY(event.getY());
            linePainting.setStrokeWidth(grosorGeneral);
            event.consume();
        }
    }
    
    public void presionarArco(MouseEvent event) {
        circlePainting = new Arc();
        circlePainting.setStroke(btnColor.getValue());
        circlePainting.setFill(javafx.scene.paint.Color.TRANSPARENT);

        circlePainting.setCenterX(event.getX());
        circlePainting.setCenterY(event.getY());
        inicioXArc = event.getX();
        inicioYArc = event.getY();
        
        circlePainting.setRadiusX(1);
        circlePainting.setRadiusY(1);
        circlePainting.setStartAngle(0);
        circlePainting.setLength(180);

        circlePainting.setType(ArcType.OPEN);

        zoomGroup.getChildren().add(circlePainting);

        circlePainting.setOnContextMenuRequested(e -> {
            ContextMenu menuContext = new ContextMenu();
            MenuItem borrarItem = new MenuItem("Eliminar");
            menuContext.getItems().add(borrarItem);
            borrarItem.setOnAction(ev -> {
                zoomGroup.getChildren().remove(circlePainting);
                ev.consume();
            });
            menuContext.show(circlePainting, e.getScreenX(), e.getScreenY());
            e.consume();
        });
    }
    
    public void arrastrarArco(MouseEvent event) {
        if (circlePainting != null) {
            double deltaY = event.getY() - inicioYArc;
            double radio = Math.abs(deltaY);
            circlePainting.setRadiusX(radio);
            circlePainting.setRadiusY(radio);
            circlePainting.setLength(180);
            
            if (deltaY >= 0) {
                circlePainting.setStartAngle(180);
            } else {
                circlePainting.setStartAngle(0);
            }
            circlePainting.setStrokeWidth(grosorGeneral);
            event.consume();
        }
    }   

    
    
    public void marcarPunto(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        
        double tamano = 2;
        Line linea1 = new Line(x - tamano, y - tamano, x + tamano, y + tamano);
        linea1.setStroke(btnColor.getValue()); 
        linea1.setStrokeWidth(2); 

        Line linea2 = new Line(x - tamano, y + tamano, x + tamano, y - tamano);
        linea2.setStroke(btnColor.getValue());
        linea2.setStrokeWidth(2); 

        Group xGroup = new Group(linea1, linea2);

        zoomGroup.getChildren().add(xGroup);
        
        xGroup.setOnContextMenuRequested(e -> {
            ContextMenu menuContext = new ContextMenu();
            MenuItem borrarItem = new MenuItem("Eliminar");
            menuContext.getItems().add(borrarItem);
            borrarItem.setOnAction(ev -> {
                zoomGroup.getChildren().remove(xGroup);
                ev.consume();
            });
            menuContext.show(xGroup, e.getScreenX(), e.getScreenY());
            e.consume();
        });
        event.consume();
    }
    
    /** 
     * Será llamado desde el controlador anterior.
     */
    /** Invocado desde el controlador anterior tras cargar el FXML */
   public void setProblema(Problem problema,User user,int h,int f,Stage s) {
        this.problema = problema;
        configurarToggleGroup();
        mostrarDatos();
        //cargarCartaImagen();
        if(user==null){
            System.out.println("User null");
            return;
        }
        this.usuario=user;
        hits = h;
        fails = f;
        myStage=s;
        myStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("Solicitud de cierre de ventana detectada.LOGIN");
            System.out.println("Guardando sesión...");
            user.addSession(hits, fails);
            System.out.println("Sesión guardada");

            // Consume el evento para evitar que la ventana se cierre inmediatamente
            // hasta que el usuario confirme o cancele.
            event.consume();
            Platform.exit();
        });
    }

    /** Agrupa los ToggleButtons para selección única */
    private void configurarToggleGroup() {
        tbResp1.setToggleGroup(respuestasGroup);
        tbResp2.setToggleGroup(respuestasGroup);
        tbResp3.setToggleGroup(respuestasGroup);
        tbResp4.setToggleGroup(respuestasGroup);
    }

    /** Rellena enunciado y ToggleButtons con el texto de las respuestas */
    private void mostrarDatos() {
        lblEnunciado.setText(problema.getText());
        Answer a1 = problema.getAnswers().get(0);
        Answer a2 = problema.getAnswers().get(1);
        Answer a3 = problema.getAnswers().get(2);
        Answer a4 = problema.getAnswers().get(3);

        tbResp1.setText(a1.getText());
        tbResp1.setUserData(a1.getValidity());
        tbResp2.setText(a2.getText());
        tbResp2.setUserData(a2.getValidity());
        tbResp3.setText(a3.getText());
        tbResp3.setUserData(a3.getValidity());
        tbResp4.setText(a4.getText());
        tbResp4.setUserData(a4.getValidity());

        // Deselecciona cualquier respuesta previa
        respuestasGroup.selectToggle(null);
        // Limpia estilos
        tbResp1.setStyle("");
        tbResp2.setStyle("");
        tbResp3.setStyle("");
        tbResp4.setStyle("");
    }

/** Aplica estilo al ToggleButton si la respuesta era correcta */
    private void marcarSiCorrecto(ToggleButton tb, boolean esValida) {
    if (esValida) {
        tb.setStyle("-fx-background-color: lightgreen;");
    } else {
        tb.setStyle("-fx-background-color: lightcoral;");
    }
}

    @FXML
    private void enviarRespuesta(ActionEvent event) {
        ToggleButton seleccionado = (ToggleButton) respuestasGroup.getSelectedToggle();
        if (seleccionado == null) {
            System.err.println("Por favor, selecciona una respuesta antes de enviar.");
            return;
        }
        // Determina validez y aplica estilo
        boolean esValida = Boolean.TRUE.equals(seleccionado.getUserData());
        if (esValida) {
            seleccionado.setStyle("-fx-background-color: lightgreen;");
            System.out.println("Respuesta correcta");
            hits++;
        } else {
            seleccionado.setStyle("-fx-background-color: lightcoral;");
            System.out.println("Respuesta incorrecta");
            fails++;
            if(Boolean.TRUE.equals(tbResp1.getUserData())){tbResp1.setStyle("-fx-background-color: lightgreen;");}
            else if(Boolean.TRUE.equals(tbResp2.getUserData())){tbResp2.setStyle("-fx-background-color: lightgreen;");}
            else if(Boolean.TRUE.equals(tbResp3.getUserData())){tbResp3.setStyle("-fx-background-color: lightgreen;");}
            else if(Boolean.TRUE.equals(tbResp4.getUserData())){tbResp4.setStyle("-fx-background-color: lightgreen;");}
        }
        // Deshabilita enviar tras corregir
        btnEnviar.setDisable(true);
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
        inicioCtrl.ini(this.usuario,hits,fails,stage);   

        stage.setScene(new Scene(root));
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.setTitle("Pantalla de Inicio");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void mostrarDialogoArco(ActionEvent event) {
        if (btnArco.isSelected()) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Configuración del Arco");
            dialog.setHeaderText("Selecciona las opciones del arco");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField radioField = new TextField();
            radioField.setPromptText("Introduce el radio del arco");
            radioField.setText(Double.toString(radioArco)); 

            TextField grosorField = new TextField();
            grosorField.setPromptText("Introduce el grosor del arco");
            grosorField.setText(Double.toString(grosorGeneral)); 

            ChoiceBox<String> radioChoiceBox = new ChoiceBox<>();
            radioChoiceBox.getItems().addAll("Radio Fijo", "Radio Cambiable");
            radioChoiceBox.setValue(radioCambiable ? "Radio Cambiable" : "Radio Fijo");

            grid.add(new Label("Radio:"), 0, 0);
            grid.add(radioField, 1, 0);
            grid.add(new Label("Grosor:"), 0, 1);
            grid.add(grosorField, 1, 1);
            grid.add(new Label("Tipo de Radio:"), 0, 2);
            grid.add(radioChoiceBox, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    radioArco = Double.parseDouble(radioField.getText());
                } catch (NumberFormatException e) {
                    radioArco = 1.0; 
                }
                try {
                    grosorGeneral = Double.parseDouble(grosorField.getText());
                } catch (NumberFormatException e) {
                    grosorGeneral = 1.0; 
                }

                radioCambiable = radioChoiceBox.getValue().equals("Radio Cambiable");

                if (radioCambiable) {
                    activarDibujoArco();
                } else {
                    activarDibujoArcoRadioFijo();
                }
            } else {
                btnArco.setSelected(false);
                btnArco.setStyle("-fx-background-color: #0B1320;");
                desactivarDibujoArco();
            }
        } else {
            desactivarDibujoArco();
        }
    }
        
    private void activarDibujoArco() {
        imageViewCarta.setOnMousePressed(this::presionarArco);
        imageViewCarta.setOnMouseDragged(this::arrastrarArco);
        btnArco.setStyle("-fx-background-color: #6c00fe;");
    }


    private void activarDibujoArcoRadioFijo() {
        imageViewCarta.setOnMousePressed(this::presionarArcoRadioFijo);
        imageViewCarta.setOnMouseDragged(null);  
        btnArco.setStyle("-fx-background-color: #6c00fe;");
    }

    private void desactivarDibujoArco() {
        imageViewCarta.setOnMousePressed(null);
        imageViewCarta.setOnMouseDragged(null);
        btnArco.setStyle("-fx-background-color: #0B1320;");
    }

    public void presionarArcoRadioFijo(MouseEvent event) {

        circlePainting = new Arc();
        circlePainting.setStroke(btnColor.getValue());
        circlePainting.setStrokeWidth(grosorGeneral);
        circlePainting.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circlePainting.setCenterX(event.getX());
        circlePainting.setCenterY(event.getY());
        circlePainting.setRadiusX(radioArco);  
        circlePainting.setRadiusY(radioArco);
        circlePainting.setStartAngle(0);
        circlePainting.setLength(180);
        circlePainting.setType(ArcType.OPEN);

        zoomGroup.getChildren().add(circlePainting);

        circlePainting.setOnContextMenuRequested(e -> {
            ContextMenu menuContext = new ContextMenu();
            MenuItem borrarItem = new MenuItem("Eliminar");
            menuContext.getItems().add(borrarItem);
            borrarItem.setOnAction(ev -> {
                zoomGroup.getChildren().remove(circlePainting);
                ev.consume();
            });
            menuContext.show(circlePainting, e.getScreenX(), e.getScreenY());
            e.consume();
        });
    }

    @FXML
    private void mostrarDialogoLinea(ActionEvent event) {
        if (btnLinea.isSelected()) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Configuración de la Línea");
            dialog.setHeaderText("Selecciona el grosor de la línea");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField grosorField = new TextField();
            grosorField.setPromptText("Introduce el grosor de la línea");
            grosorField.setText(Double.toString(grosorGeneral)); 

            grid.add(new Label("Grosor:"), 0, 0);
            grid.add(grosorField, 1, 0);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    grosorGeneral = Double.parseDouble(grosorField.getText());
                } catch (NumberFormatException e) {
                    grosorGeneral = 1.0; 
                }
                activarDibujoLinea();
            } else {
                btnLinea.setSelected(false);
                btnLinea.setStyle("-fx-background-color: #0B1320;"); 
                desactivarDibujoLinea();
            }
        } else {
            desactivarDibujoLinea();
        }
    }
    
        private void activarDibujoLinea() {
            imageViewCarta.setOnMousePressed(this::presionarLinea);
            imageViewCarta.setOnMouseDragged(this::arrastrarLinea);
            btnLinea.setStyle("-fx-background-color: #6c00fe;");
        }
        
        private void desactivarDibujoLinea() {
            imageViewCarta.setOnMousePressed(null);
            imageViewCarta.setOnMouseDragged(null);
            btnLinea.setStyle("-fx-background-color: #0B1320;");
        }

    private void mostrarDialogoTexto(MouseEvent event) {
        if (btnText.isSelected()) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Configuración del Texto");
            dialog.setHeaderText("Introduce el tamaño del texto");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField sizeField = new TextField();
            sizeField.setPromptText("Introduce el tamaño del texto");
            sizeField.setText("12");

            grid.add(new Label("Tamaño del texto:"), 0, 0);
            grid.add(sizeField, 1, 0);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    double tamanoTexto = Double.parseDouble(sizeField.getText());
                    agregarTexto(tamanoTexto);
                } catch (NumberFormatException e) {
                    agregarTexto(12.0);
                }
            } else {
                btnText.setSelected(false);
                btnText.setStyle("-fx-background-color: #0B1320;");
            }
        }

    }
    
    public void agregarTexto(double tamanoTexto) {
        imageViewCarta.setOnMousePressed(event -> {
            TextField texto = new TextField();
            texto.setPrefWidth(100); 
            texto.setPrefHeight(30); 

            zoomGroup.getChildren().add(texto);
            texto.setLayoutX(event.getX());
            texto.setLayoutY(event.getY());
            texto.requestFocus();

            texto.setOnAction(e -> {
                Text textoT = new Text(texto.getText());
                textoT.setX(texto.getLayoutX());
                textoT.setY(texto.getLayoutY());
                textoT.setStroke(btnColor.getValue());  
                textoT.setFont(javafx.scene.text.Font.font(tamanoTexto));  
                zoomGroup.getChildren().add(textoT);  
                zoomGroup.getChildren().remove(texto);
                e.consume();  
                textoT.setOnContextMenuRequested(ee -> {
                    ContextMenu menuContext = new ContextMenu();
                    MenuItem borrarItem = new MenuItem("Eliminar");
                    menuContext.getItems().add(borrarItem);
                    borrarItem.setOnAction(ev -> {
                        zoomGroup.getChildren().remove(textoT);
                        ev.consume();
                    });
                    menuContext.show(textoT, ee.getScreenX(), ee.getScreenY());
                    ee.consume();
                });
            });
        });
    }


    public void borrarTodo(){
        zoomGroup.getChildren().removeIf(node -> 
        node instanceof Line ||
        node instanceof Arc ||
        node instanceof Text ||
        node instanceof Group 
        );
    }
}


    