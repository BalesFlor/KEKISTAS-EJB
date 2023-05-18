package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoUsuario;
import java.util.List;

public interface EstadoUsuarioBeanRemote {
    List<EstadoUsuario> listarEstadosUsuario();
}
