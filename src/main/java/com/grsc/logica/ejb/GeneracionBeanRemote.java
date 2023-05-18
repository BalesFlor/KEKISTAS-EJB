package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Generacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface GeneracionBeanRemote {
    Generacion buscarGen(BigInteger anio);
    List<Generacion> listarGeneracion();
}
