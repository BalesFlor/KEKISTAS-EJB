package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoUsuario;
import java.math.BigInteger;
import java.util.List;

public interface EstadoUsuarioBeanRemote {
    List<EstadoUsuario> listarEstadosUsuario();
    EstadoUsuario buscar(BigInteger id);
}
