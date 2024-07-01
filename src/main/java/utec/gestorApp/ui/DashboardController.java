package utec.gestorApp.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import utec.gestorApp.usuario.Rol;
import utec.gestorApp.usuario.Usuario;
import utec.gestorApp.usuario.UsuarioRepository;

@Component
public class DashboardController implements Initializable {

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private VBox rootVBox;

    @FXML
    private Button adminButton;

    @FXML
    private Button constanciasButton;

    @FXML
    private Button eventosButton;

    @FXML
    private Button historialButton;

    @FXML
    private Button reportesButton;

    @FXML
    private Button reclamosButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label rolLabel;

    private Usuario usuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            Platform.runLater(this::mostrarAlertaAutenticacion);
        } else {
            org.springframework.security.core.userdetails.User springUser =
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            this.usuario = convertToUsuario(springUser);

            if (!usuario.isActivo()) {
                Platform.runLater(this::mostrarAlertaActivacion);
            } else {
                configureButtonVisibilityBasedOnRole(usuario.getRol().getNombre());
                usernameLabel.setText(usuario.getUsername());
                rolLabel.setText(usuario.getRol().getNombre());
            }
        }

        logoutButton.setOnAction(event -> logout());

        adminButton.setOnAction(event -> {
            try {
                openAdminView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        constanciasButton.setVisible(false); // Hacer invisible el botón de constancias

        eventosButton.setOnAction(event -> {
            try {
                openEventosView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        historialButton.setVisible(false); // Hacer invisible el botón de historial

        reportesButton.setOnAction(event -> {
            try {
                openReportesView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        reclamosButton.setVisible(false); // Hacer invisible el botón de reclamos
    }

    private void openReportesView() {
        try {
            Stage primaryStage = (Stage) rootVBox.getScene().getWindow();
            sceneManager.switchScene(primaryStage, "/fxml/ReportePage.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Usuario convertToUsuario(org.springframework.security.core.userdetails.User user) {
        Usuario usuarioFromDb = usuarioRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + user.getUsername()));
        return usuarioFromDb;
    }

    private void mostrarAlertaAutenticacion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Autenticación");
        alert.setHeaderText("No se pudo autenticar al usuario.");
        alert.setContentText("Por favor, inicie sesión nuevamente.");
        alert.showAndWait();

        try {
            Stage window = (Stage) rootVBox.getScene().getWindow();
            sceneManager.switchScene(window, "/fxml/LoginView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaActivacion() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Activación Necesaria");
        alert.setHeaderText("Su cuenta aún no ha sido activada.");
        alert.setContentText("Para continuar, por favor contacte al administrador.");
        alert.showAndWait();
    }

    private void configureButtonVisibilityBasedOnRole(String rolNombre) {
        boolean isAdmin = Rol.ANALISTA.equals(rolNombre);
        boolean isDocente = Rol.DOCENTE.equals(rolNombre);
        boolean isEstudiante = Rol.ESTUDIANTE.equals(rolNombre);

        //Cuando sean implementados, los demás botones se harán visibles
        adminButton.setVisible(isAdmin);
        constanciasButton.setVisible(false);
        eventosButton.setVisible(isAdmin || isDocente);
        historialButton.setVisible(false);
        reportesButton.setVisible(isAdmin || isDocente);
        reclamosButton.setVisible(false);
    }

    private void logout() {
        SecurityContextHolder.clearContext();
        try {
            Stage window = (Stage) rootVBox.getScene().getWindow();
            sceneManager.switchScene(window, "/fxml/LoginView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAdminView() throws Exception {
        try {
            System.out.println("Loading Admin View...");
            Stage window = (Stage) adminButton.getScene().getWindow();
            sceneManager.switchScene(window, "/fxml/AdminView.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load Admin View: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void openEventosView() throws Exception {
        try {
            System.out.println("Loading Eventos View...");
            Stage window = (Stage) eventosButton.getScene().getWindow();
            sceneManager.switchScene(window, "/fxml/EventsView.fxml");
        } catch (IOException e) {
            System.err.println("Failed to load Eventos View: " + e.getMessage());
            e.printStackTrace();
        }
    }
}