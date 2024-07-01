package utec.gestorApp.ui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utec.gestorApp.usuario.Usuario;
import utec.gestorApp.usuario.UsuarioService;

@Component
public class AnalistaControllerFx implements Initializable {

    @Autowired
    private UsuarioService usuarioService;

    @FXML
    private TableView<Usuario> usuariosTable;

    @FXML
    private TableColumn<Usuario, String> usernameColumn;
    @FXML
    private TableColumn<Usuario, String> cedulaColumn;
    @FXML
    private TableColumn<Usuario, String> fechaNacimientoColumn;
    @FXML
    private TableColumn<Usuario, String> telefonoColumn;
    @FXML
    private TableColumn<Usuario, String> emailColumn;
    @FXML
    private TableColumn<Usuario, String> departamentoColumn;
    @FXML
    private TableColumn<Usuario, String> localidadColumn;
    @FXML
    private TableColumn<Usuario, String> generacionColumn;
    @FXML
    private TableColumn<Usuario, String> semestreColumn;
    @FXML
    private TableColumn<Usuario, String> itrColumn;
    @FXML
    private TableColumn<Usuario, Boolean> activoColumn;
    @FXML
    private TableColumn<Usuario, String> areaColumn;
    @FXML
    private Button backButton;

    @Autowired
    private SceneManager sceneManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureCellFactories();
        cargarUsuarios();
    }

    private void configureCellFactories() {
        usernameColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getUsername()));
        cedulaColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getCedula()));
        fechaNacimientoColumn.setCellValueFactory(cellData -> stringValue(formatDate(cellData.getValue().getFechaNacimiento())));
        telefonoColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getTelefono()));
        emailColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getEmail()));
        departamentoColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getDepartamento()));
        localidadColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getLocalidad()));
        generacionColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getGeneracion()));
        semestreColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getSemestre()));
        itrColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getItr()));
        activoColumn.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            SimpleBooleanProperty observable = new SimpleBooleanProperty(usuario.isActivo());
            observable.addListener((obs, oldValue, newValue) -> {
                usuario.setActivo(newValue);
                usuarioService.actualizarUsuario(usuario);
            });
            return observable;
        });
        activoColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
        areaColumn.setCellValueFactory(cellData -> stringValue(cellData.getValue().getArea()));
    }

    private SimpleStringProperty stringValue(String value) {
        return new SimpleStringProperty(value != null ? value : "");
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }


    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        usuariosTable.getItems().setAll(usuarios);
    }

    @FXML
    private void openDashboardView() throws IOException {
        Stage window = (Stage) usuariosTable.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/Dashboard.fxml");
    }
}