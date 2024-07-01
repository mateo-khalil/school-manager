package utec.gestorApp.evento;

import org.springframework.data.jpa.repository.JpaRepository;
import utec.gestorApp.usuario.Usuario;

import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    boolean existsByEventoAndUsuario(Evento evento, Usuario usuario);

    Optional<Asistencia> findByEventoAndUsuario(Evento evento, Usuario usuario);
}
