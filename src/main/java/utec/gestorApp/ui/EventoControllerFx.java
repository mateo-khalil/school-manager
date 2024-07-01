package utec.gestorApp.ui;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utec.gestorApp.evento.Evento;
import utec.gestorApp.evento.EventoListCell;
import utec.gestorApp.evento.EventoService;

@Component
public class EventoControllerFx {

    @Autowired
    private EventoService eventoService;

    @FXML
    private TextField nombreEventoField;

    @FXML
    private ComboBox<Evento.Tipo> tipoEventoComboBox;

    @FXML
    private Button crearEventoButton;

    @FXML
    private ListView<Evento> eventosListView;

    @FXML
    private Label statusLabel;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        crearEventoButton.setOnAction(event -> crearEvento());
        eventosListView.setCellFactory(param -> new EventoListCell(this));
        listarEventos();

        ObservableList<Evento.Tipo> tiposEvento = FXCollections.observableArrayList(Evento.Tipo.values());
        tipoEventoComboBox.setItems(tiposEvento);
        backButton.setOnAction(event -> {
            try {
                openDashboardView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void crearEvento() {
        String nombre = nombreEventoField.getText();
        Evento.Tipo tipo = tipoEventoComboBox.getValue();
        Evento evento = eventoService.crearEvento(nombre, tipo);
        eventosListView.getItems().add(evento);
        statusLabel.setText("Evento creado: " + evento.getNombre());
    }

    @FXML
    private void listarEventos() {
        eventosListView.getItems().setAll(eventoService.listarEventos());
    }

    public void mostrarOpcionesEvento(Evento evento) {
        try {
            Stage currentStage = (Stage) eventosListView.getScene().getWindow();
            sceneManager.switchSceneWithData(currentStage, "/fxml/EventOptionsView.fxml", evento);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar la vista de opciones del evento.");
        }
    }

    @FXML
    private void openDashboardView() throws Exception {
        Stage window = (Stage) backButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/Dashboard.fxml");
    }
}