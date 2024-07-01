package utec.gestorApp.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }
}