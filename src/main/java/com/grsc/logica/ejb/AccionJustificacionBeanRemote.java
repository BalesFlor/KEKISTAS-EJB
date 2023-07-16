package com.grsc.logica.ejb;

import com.grsc.modelo.entities.AccionJustificacion;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Justificacion;
import java.util.Date;

public interface AccionJustificacionBeanRemote {
    Boolean registrarAccion(Justificacion justificacion, Analista analista, String detalle, Date fechaHora);
    AccionJustificacion buscarAccionJustificacion(Justificacion justificacion, Analista analista);
    Boolean modificarAccion(Justificacion justificacion, Analista analista, String detalle, Date fechaHora);
}
