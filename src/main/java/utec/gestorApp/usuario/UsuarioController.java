package utec.gestorApp.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @GetMapping("/activar/{id}")
    public ResponseEntity<String> activarUsuario(@PathVariable Long id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.ok("Usuario activado con éxito");
    }
}
