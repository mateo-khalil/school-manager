package utec.gestorApp.ui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utec.gestorApp.usuario.Rol;
import utec.gestorApp.usuario.RolService;
import utec.gestorApp.usuario.Usuario;
import utec.gestorApp.usuario.UsuarioService;

@Component
public class RegistroController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(RegistroController.class.getName());

    @Autowired
    private SceneManager sceneManager;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField cedulaField;

    @FXML
    private TextField fechaNacimientoField;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField departamentoField;

    @FXML
    private TextField localidadField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField generacionField;

    @FXML
    private TextField semestreField;

    @FXML
    private ComboBox<String> itrField;

    @FXML
    private ComboBox<Rol> rolField;

    @FXML
    private TextField areaField;

    @FXML
    private TextField rolEnAreaField;

    @FXML
    public Button backButton;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioService usuarioService;

    public RegistroController() {}

    @Autowired
    public RegistroController(RolService rolService, UsuarioService usuarioService) {
        this.rolService = rolService;
        this.usuarioService = usuarioService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> itrOptions = Arrays.asList("Fray Bentos", "Mercedes", "Rivera", "Paysandú", "Nueva Helvecia", "Durazno", "Colonia Valdense");
        if (itrField != null) {
            itrField.getItems().setAll(itrOptions);
        } else {
            LOGGER.severe("itrField is null. Check FXML injection.");
        }
        cargarRoles();
        areaField.setVisible(false);
        rolEnAreaField.setVisible(false);
        
        rolField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ("Docente".equals(newValue.getNombre())) {
                areaField.setVisible(true);
                rolEnAreaField.setVisible(true);
            } else {
                areaField.setVisible(false);
                rolEnAreaField.setVisible(false);
            }
        });
    }

    private void cargarRoles() {
        List<Rol> roles = Rol.getPredefinedRoles();
        rolField.getItems().setAll(roles);
        rolField.setConverter(new StringConverter<Rol>() {
            @Override
            public String toString(Rol rol) {
                return rol.getNombre();
            }

            @Override
            public Rol fromString(String string) {
                return rolField.getItems().stream().filter(rol ->
                        rol.getNombre().equals(string)).findFirst().orElse(null);
            }
        });
    }

    @FXML
    public void registrarUsuario() {
        StringBuilder errorMessage = new StringBuilder();

        if (usernameField.getText().trim().isEmpty()) {
            errorMessage.append("Username is required.\n");
        }
        if (cedulaField.getText().trim().isEmpty()) {
            errorMessage.append("Cedula is required.\n");
        }
        if (telefonoField.getText().trim().isEmpty()) {
            errorMessage.append("Telefono is required.\n");
        }
        if (departamentoField.getText().trim().isEmpty()) {
            errorMessage.append("Departamento is required.\n");
        }
        if (localidadField.getText().trim().isEmpty()) {
            errorMessage.append("Localidad is required.\n");
        }
        if (!isValidInstitutionalEmail(emailField.getText())) {
            errorMessage.append("Invalid institutional email format. It must belong to the domain utec.edu.uy.\n");
        }
        if (!isValidPassword(passwordField.getText())) {
            errorMessage.append("Password must be at least 8 characters long and contain both letters and numbers.\n");
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usernameField.getText());
        usuario.setCedula(cedulaField.getText());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaNacimiento = dateFormat.parse(fechaNacimientoField.getText());
            usuario.setFechaNacimiento(fechaNacimiento);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        usuario.setTelefono(telefonoField.getText());
        usuario.setEmail(emailField.getText());
        usuario.setDepartamento(departamentoField.getText());
        usuario.setLocalidad(localidadField.getText());
        usuario.setGeneracion(generacionField.getText());
        usuario.setSemestre(semestreField.getText());
        usuario.setItr(itrField.getValue());
        usuario.setPassword(passwordField.getText());

        Rol selectedRol = rolField.getValue();
        usuario.setRol(selectedRol);

        if ("Tutor".equals(selectedRol.getNombre())) {
            usuario.setArea(areaField.getText());
        }

        usuarioService.registrarUsuario(usuario);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Registro Exitoso");
        successAlert.setHeaderText(null);
        successAlert.setContentText("El registro ha sido enviado con éxito. Estará disponible una vez que un administrador lo active.");
        successAlert.showAndWait();

        try {
            openWelcomeView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidInstitutionalEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@utec.edu.uy$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");
    }

    @FXML
    private void openWelcomeView() throws Exception {
        Stage window = (Stage) backButton.getScene().getWindow();
        sceneManager.switchScene(window, "/fxml/WelcomeView.fxml");
    }
}