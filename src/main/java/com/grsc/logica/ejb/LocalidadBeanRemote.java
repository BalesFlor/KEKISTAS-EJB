package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Localidad;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface LocalidadBeanRemote {
    String[] listarNomLocalsByDepto(BigInteger idDepto);
    List<Localidad> listarLocals();
    List<Localidad> listarLocalsByDepto(BigInteger idDepto);
    int obtenerCantidadLocalidades(BigInteger idDepto);
}
