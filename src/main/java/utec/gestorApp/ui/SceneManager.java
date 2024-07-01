package utec.gestorApp.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import utec.gestorApp.evento.Evento;
import utec.gestorApp.evento.EventoOpcionesController;

import java.io.IOException;

@Component
public class SceneManager {
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    private final ApplicationContext applicationContext;

    public SceneManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void switchScene(Stage stage, String fxmlFilePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        stage.setScene(scene);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.show();
    }
    // In SceneManager.java
    public void switchSceneWithData(Stage stage, String fxmlFilePath, Object data) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        loader.setControllerFactory(applicationContext::getBean);
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Object controller = loader.getController();
        if (controller instanceof EventoOpcionesController && data instanceof Evento) {
            ((EventoOpcionesController) controller).setEvento((Evento) data);
        }

        stage.setScene(scene);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.show();
    }
}