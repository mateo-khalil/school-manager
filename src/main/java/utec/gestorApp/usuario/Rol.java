package utec.gestorApp.usuario;

import java.util.Arrays;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Rol {

    /* Atributos */

    public static final String ANALISTA = "Analista";
    public static final String ESTUDIANTE = "Estudiante";
    public static final String DOCENTE = "Docente";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;

    /* Métodos */

    public static List<Rol> getPredefinedRoles() {
        return Arrays.asList(
                new Rol(ANALISTA),
                new Rol(ESTUDIANTE),
                new Rol(DOCENTE)
        );
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Rol() {
    }

    public String getNombre() {
        return nombre;
    }
}