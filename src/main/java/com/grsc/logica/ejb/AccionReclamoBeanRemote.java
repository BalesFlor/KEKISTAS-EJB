package com.grsc.logica.ejb;

import com.grsc.modelo.entities.AccionReclamo;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Reclamo;
import java.util.Date;

public interface AccionReclamoBeanRemote {
    Boolean registrarAccion(Reclamo reclamo, Analista analista, String detalle, Date fechaHora);
    AccionReclamo buscarAccionReclamo(Analista analista, Date FechaHora);
}
