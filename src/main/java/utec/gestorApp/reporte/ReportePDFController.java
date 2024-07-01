package utec.gestorApp.reporte;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utec.gestorApp.evento.Evento;
import utec.gestorApp.evento.EventoService;
import utec.gestorApp.ui.SceneManager;
import utec.gestorApp.usuario.Usuario;

import java.util.List;

@Component
public class ReportePDFController {

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private ComboBox<Evento> eventoComboBox;

    @Autowired
    private EventoService eventoService;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        List<Evento> eventos = eventoService.listarEventos();
        ObservableList<Evento> eventosList = FXCollections.observableArrayList(eventos);
        eventoComboBox.setItems(eventosList);
        eventoComboBox.setConverter(new EventoStringConverter());

        backButton.setOnAction(event -> {
            try {
                openEventsView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void generarReportePDF() {
        Evento evento = eventoComboBox.getValue();

        if (evento != null) {
            try {
                List<Usuario> usuariosInscritos = eventoService.obtenerUsuariosInscritos(evento.getId());
                ReportGenerator reportePDF = new ReportGenerator(eventoService);
                Stage stage = (Stage) eventoComboBox.getScene().getWindow();
                reportePDF.generarReporte(evento, usuariosInscritos, stage);
            } catch (Exception e) {
                mostrarAlerta("Error al generar el reporte", e.getMessage());
            }
        } else {
            mostrarAlerta("Selección requerida", "Por favor, seleccione un evento para generar el reporte.");
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    private void openEventsView() throws Exception {
        Stage window = (Stage) backButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/Dashboard.fxml");
    }
    private static class EventoStringConverter extends javafx.util.StringConverter<Evento> {

        @Override
        public String toString(Evento evento) {
            return evento != null ? evento.getNombre() : null;
        }

        @Override
        public Evento fromString(String string) {
            return null;
        }
    }
}