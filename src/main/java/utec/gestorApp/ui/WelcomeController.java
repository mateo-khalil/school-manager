package utec.gestorApp.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WelcomeController {

    public Button loginButton;
    public Button registerButton;

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private void openLogin() throws Exception {
        Stage window = (Stage) loginButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/LoginView.fxml");
    }
    
    @FXML
    private void openRegister() throws Exception {
        Stage window = (Stage) registerButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/RegisterView.fxml");
    }

}
