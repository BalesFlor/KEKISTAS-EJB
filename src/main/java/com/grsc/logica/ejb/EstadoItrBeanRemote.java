package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoItr;
import java.math.BigInteger;
import java.util.List;

public interface EstadoItrBeanRemote {
    List<EstadoItr> listarEstados();
    EstadoItr buscar(BigInteger id);
    EstadoItr buscarPorNom(String nom);
}
