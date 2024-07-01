package utec.gestorApp.usuario;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    /* Variables */

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    /* Métodos */

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        Rol rol = usuario.getRol();
        if (rol != null) {
            Rol existingRol = rolRepository.findByNombre(rol.getNombre()).orElse(null);
            if (existingRol == null) {
                existingRol = rolRepository.save(rol);
            }
            usuario.setRol(existingRol);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public void activarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public boolean loginUsuario(String username, String password) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return passwordEncoder.matches(password, usuario.getPassword());
        }
        return false;
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

  public Rol obtenerRolPorNombre(String nombreRol) {
    return rolRepository.findByNombre(nombreRol).orElse(null);
    }

    @Transactional
    public Usuario actualizarUsuario(Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioActualizado.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setCedula(usuarioActualizado.getCedula());
        usuarioExistente.setFechaNacimiento(usuarioActualizado.getFechaNacimiento());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setDepartamento(usuarioActualizado.getDepartamento());
        usuarioExistente.setLocalidad(usuarioActualizado.getLocalidad());
        usuarioExistente.setGeneracion(usuarioActualizado.getGeneracion());
        usuarioExistente.setSemestre(usuarioActualizado.getSemestre());
        usuarioExistente.setItr(usuarioActualizado.getItr());
        usuarioExistente.setActivo(usuarioActualizado.isActivo());
        usuarioExistente.setArea(usuarioActualizado.getArea());
        if (!usuarioActualizado.getPassword().equals(usuarioExistente.getPassword())) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }
        Rol rol = usuarioActualizado.getRol();
        if (rol != null) {
            Rol existingRol = rolRepository.findByNombre(rol.getNombre()).orElse(null);
            if (existingRol == null) {
                existingRol = rolRepository.save(rol);
            }
            usuarioExistente.setRol(existingRol);
        }
        usuarioExistente.setRol(usuarioActualizado.getRol());
        return usuarioRepository.save(usuarioExistente);
    }
}
