package utec.gestorApp.evento;


import utec.gestorApp.usuario.Usuario;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Evento {
    public enum Tipo { EXAMEN, CONVOCATORIA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @ManyToMany
    @JoinTable(
            name = "evento_estudiantes",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> estudiantesInscritos = new ArrayList<>();

    private boolean confirmado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<Usuario> getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    public void setEstudiantesInscritos(List<Usuario> estudiantesInscritos) {
        this.estudiantesInscritos = estudiantesInscritos;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}
