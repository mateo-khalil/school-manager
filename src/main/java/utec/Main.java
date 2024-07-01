package utec;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import utec.gestorApp.ui.SceneManager;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() {
        context.close();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager sceneManager = context.getBean(SceneManager.class);
            sceneManager.switchScene(primaryStage, "/fxml/WelcomeView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}