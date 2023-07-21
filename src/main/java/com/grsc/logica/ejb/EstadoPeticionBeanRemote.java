package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoPeticion;
import java.math.BigInteger;
import java.util.List;


public interface EstadoPeticionBeanRemote {
    List<EstadoPeticion> listarEstados();
    EstadoPeticion buscar(BigInteger id);
    EstadoPeticion buscarPorNom(String nom);
}
