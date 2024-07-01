package utec.gestorApp.evento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public List<Evento> listarEventos() {
        return eventoService.listarEventos();
    }

    @PostMapping
    public Evento crearEvento(@RequestBody Evento evento) {
        return eventoService.crearEvento(evento.getNombre(), evento.getTipo());
    }

    @PostMapping("/{eventoId}/inscribir/{usuarioId}")
    public void inscribirUsuario(@PathVariable Long eventoId, @PathVariable Long usuarioId) {
        eventoService.inscribirUsuario(eventoId, usuarioId);
    }

    @PostMapping("/{eventoId}/confirmar")
    public void confirmarEvento(@PathVariable Long eventoId) {
        eventoService.confirmarEvento(eventoId);
    }

    @PostMapping("/{eventoId}/asistencia/{usuarioId}")
    public void registrarAsistencia(@PathVariable Long eventoId, @PathVariable Long usuarioId) {
        eventoService.registrarAsistencia(eventoId, usuarioId);
    }
}
