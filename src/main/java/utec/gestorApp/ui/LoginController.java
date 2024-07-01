package utec.gestorApp.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    public void loginUsuario() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Error de Inicio de Sesión", "Credenciales inválidas",
                    "Por favor, ingrese tanto el nombre de usuario como la contraseña.");
            LOGGER.warning("Intento de inicio de sesión sin proporcionar todas las credenciales.");
            return;
        }

        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
            Authentication result = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(result);

            if (result.isAuthenticated()) {
                LOGGER.info("Inicio de sesión exitoso para el usuario: " + username);
                openDashboardView();
            } else {
                showErrorAlert("Error de Inicio de Sesión", "Inicio de Sesión Fallido",
                        "Nombre de usuario o contraseña inválidos.");
                LOGGER.warning("Inicio de sesión fallido para el usuario: " + username);
            }
        } catch (DisabledException e) {
            showErrorAlert("Cuenta Deshabilitada", "Cuenta de Usuario Deshabilitada",
                    "Su cuenta de usuario está deshabilitada. Por favor, contacte al administrador.");
            LOGGER.severe("Cuenta deshabilitada para el usuario: " + username);
        } catch (AuthenticationException e) {
            showErrorAlert("Error de Inicio de Sesión", "Error de Autenticación",
                    "Un error de autenticación ha ocurrido. Por favor, intente nuevamente.");
            LOGGER.severe("Error de autenticación para el usuario: " + username + ". Error: " + e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Error de Inicio de Sesión", "Error del Sistema",
                    "Un error inesperado ha ocurrido. Por favor, contacte al administrador del sistema.");
            e.printStackTrace();
            LOGGER.severe("Error inesperado durante el inicio de sesión para el usuario: " + username + ". Error: " + e.getMessage());
        }
    }

    private void showErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    private void openDashboardView() throws Exception {
        Stage window = (Stage) loginButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/Dashboard.fxml");
    }

    @FXML
    private void openWelcomeView() throws Exception {
        Stage window = (Stage) backButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/WelcomeView.fxml");
    }
}