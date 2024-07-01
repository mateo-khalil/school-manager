package utec.gestorApp.usuario;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
public class Usuario {

    /* Atributos */

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String username;
    @Getter
    private String cedula;
    @Getter
    private Date fechaNacimiento;
    @Getter
    private String telefono;
    @Getter
    private String email;
    @Getter
    private String departamento;
    @Getter
    private String localidad;
    @Getter
    private String generacion;
    @Getter
    private String semestre;
    @Getter
    private String itr;
    @Getter
    private String password;
    @Getter
    private boolean activo;
    @Getter
    private String area;

    @ManyToOne
    private Rol rol;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles;

    /* Métodos */

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setItr(String itr) {
        this.itr = itr;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCedula() {
        return cedula;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getGeneracion() {
        return generacion;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getItr() {
        return itr;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getArea() {
        return area;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.roles = authorities.stream()
                .map(authority -> new Rol(authority.getAuthority()))
                .collect(Collectors.toSet());
    }

    public StringProperty usernameProperty() {
        return new SimpleStringProperty(username);
    }

    public BooleanProperty activoProperty() {
        return new SimpleBooleanProperty(activo);
    }
}