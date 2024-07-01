package utec.gestorApp.evento;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utec.gestorApp.ui.SceneManager;
import utec.gestorApp.usuario.Usuario;
import utec.gestorApp.usuario.UsuarioService;

@Component
public class EventoOpcionesController {

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private TableView<Usuario> usuariosInscriptosTableView;

    @FXML
    private TableColumn<Usuario, String> InscriptedusernameColumn;

    @FXML
    private TableColumn<Usuario, String> InscriptedcedulaColumn;

    @FXML
    private TableColumn<Usuario, String> InscriptedemailColumn;

    @FXML
    private TableColumn<Usuario, String> InscripteddepartamentoColumn;

    @FXML
    private TableColumn<Usuario, String> InscriptedsemestreColumn;

    @FXML
    private TableColumn<Usuario, Boolean> InscriptedasistioColumn;

    @FXML
    private TableView<Usuario> todosUsuariosTableView;

    @FXML
    private TableColumn<Usuario, String> usernameColumn;

    @FXML
    private TableColumn<Usuario, String> cedulaColumn;

    @FXML
    private TableColumn<Usuario, String> emailColumn;

    @FXML
    private TableColumn<Usuario, String> departamentoColumn;

    @FXML
    private TableColumn<Usuario, String> semestreColumn;

    @FXML
    private TableColumn<Usuario, Boolean> isInscriptedColumn;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoService eventoService;

    @FXML
    private Label eventoNombreLabel;

    @FXML
    private Button agregarUsuarioButton;

    private Evento evento;

    @FXML
    private Button backButton;

    public void setEvento(Evento evento) {
        this.evento = evento;
        eventoNombreLabel.setText("Opciones del Evento: " + evento.getNombre());
        listarUsuariosInscriptos();
        listarTodosUsuarios();
    }

    @FXML
    public void initialize() {
        agregarUsuarioButton.setOnAction(event -> agregarUsuario());
        InscriptedusernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        InscriptedcedulaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedula()));
        InscriptedemailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        InscripteddepartamentoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartamento()));
        InscriptedsemestreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemestre()));
        InscriptedasistioColumn.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(eventoService.usuarioAsistio(evento.getId(), usuario.getId()));
            property.addListener((obs, oldValue, newValue) -> {
                eventoService.toggleAsistencia(evento.getId(), usuario.getId(), newValue);
                listarUsuariosInscriptos();
            });
            return property;
        });
        InscriptedasistioColumn.setCellFactory(CheckBoxTableCell.forTableColumn(InscriptedasistioColumn));

        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        cedulaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedula()));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        departamentoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartamento()));
        semestreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemestre()));
        isInscriptedColumn.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            BooleanProperty property = new SimpleBooleanProperty(eventoService.usuarioInscrito(evento.getId(), usuario.getId()));
            property.addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    eventoService.inscribirUsuario(evento.getId(), usuario.getId());
                } else {
                    eventoService.desinscribirUsuario(evento.getId(), usuario.getId());
                }
                listarUsuariosInscriptos();
                listarTodosUsuarios();
            });
            return property;
        });
        isInscriptedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(isInscriptedColumn));
        todosUsuariosTableView.setEditable(true);
        usuariosInscriptosTableView.setEditable(true);

        backButton.setOnAction(event -> {
            try {
                openEventsView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void listarUsuariosInscriptos() {
        try {
            List<Usuario> inscriptos = eventoService.obtenerUsuariosInscritos(evento.getId());
            usuariosInscriptosTableView.getItems().setAll(inscriptos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listarTodosUsuarios() {
        try {
            List<Usuario> todosUsuarios = usuarioService.obtenerTodosLosUsuarios();
            todosUsuariosTableView.getItems().setAll(todosUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void agregarUsuario() {
        Usuario usuario = todosUsuariosTableView.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            eventoService.inscribirUsuario(evento.getId(), usuario.getId());
            listarUsuariosInscriptos();
            listarTodosUsuarios();
        }
    }

    @FXML
    private void openEventsView() throws Exception {
        Stage window = (Stage) backButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/eventsView.fxml");
    }
}