package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Reclamo;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author fbale
 */
public interface ReclamoBeanRemote {
   List<Reclamo> listaTodosReclamos();
}
