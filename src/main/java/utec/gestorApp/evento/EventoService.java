package utec.gestorApp.evento;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utec.gestorApp.usuario.Usuario;
import utec.gestorApp.usuario.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    @Transactional
    public Evento crearEvento(String nombre, Evento.Tipo tipo) {
        Evento evento = new Evento();
        evento.setNombre(nombre);
        evento.setTipo(tipo);
        return eventoRepository.save(evento);
    }

    @Transactional
    public void inscribirUsuario(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));
        Hibernate.initialize(evento.getEstudiantesInscritos());

        if (!evento.getEstudiantesInscritos().contains(usuario)) {
            evento.getEstudiantesInscritos().add(usuario);
            eventoRepository.save(evento);
        }
    }
    @Transactional
    public void confirmarEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId).orElseThrow();
        evento.setConfirmado(true);
        eventoRepository.save(evento);
    }

    @Transactional
    public void registrarAsistencia(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));

        if (!estaUsuarioInscrito(eventoId, usuarioId)) {
            throw new RuntimeException("Usuario no inscrito en el evento");
        }

        boolean yaAsistio = asistenciaRepository.existsByEventoAndUsuario(evento, usuario);
        if (yaAsistio) {
            throw new RuntimeException("El usuario ya ha sido registrado como asistente para este evento");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setEvento(evento);
        asistencia.setUsuario(usuario);
        asistencia.setAsistio(true);
        asistenciaRepository.save(asistencia);
    }

    @Transactional
    public void toggleAsistencia(Long eventoId, Long usuarioId, boolean asistio) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));

        Optional<Asistencia> asistenciaOpt = asistenciaRepository.findByEventoAndUsuario(evento, usuario);

        if (asistenciaOpt.isPresent()) {
            Asistencia asistencia = asistenciaOpt.get();
            asistencia.setAsistio(asistio);
            asistenciaRepository.save(asistencia);
        } else if (asistio) {
            Asistencia asistencia = new Asistencia();
            asistencia.setEvento(evento);
            asistencia.setUsuario(usuario);
            asistencia.setAsistio(asistio);
            asistenciaRepository.save(asistencia);
        }
    }


    @Transactional
    public void desinscribirUsuario(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));

        if (evento.getEstudiantesInscritos().contains(usuario)) {
            evento.getEstudiantesInscritos().remove(usuario);
            eventoRepository.save(evento);
        } else {
            throw new RuntimeException("Usuario no inscrito en el evento");
        }
    }

    @Transactional
    public List<Usuario> obtenerUsuariosInscritos(Long eventoId) {
        Evento evento = eventoRepository.findByIdWithEstudiantesInscritos(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        return new ArrayList<>(evento.getEstudiantesInscritos());
    }

    @Transactional
    public boolean estaUsuarioInscrito(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));
        return evento.getEstudiantesInscritos().contains(usuario);
    }

    @Transactional
    public boolean usuarioAsistio(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));

        Optional<Asistencia> asistencia = asistenciaRepository.findByEventoAndUsuario(evento, usuario);

        return asistencia.isPresent() && asistencia.get().isAsistio();
    }

    @Transactional
    public boolean usuarioInscrito(Long eventoId, Long usuarioId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario not found"));
        return evento.getEstudiantesInscritos().contains(usuario);
    }

}
