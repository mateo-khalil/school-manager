package utec.gestorApp.evento;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    @Query("SELECT e FROM Evento e JOIN FETCH e.estudiantesInscritos WHERE e.id = :eventoId")
    Optional<Evento> findByIdWithEstudiantesInscritos(@Param("eventoId") Long eventoId);
}
