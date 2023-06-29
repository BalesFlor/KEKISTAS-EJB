package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Reclamo;
import com.grsc.modelo.entities.Evento;
import java.util.Date;
import java.util.List;
import java.math.BigInteger;

public interface ReclamoBeanRemote {
   List<Reclamo> listaTodosReclamos();
   Boolean altaReclamo(String titulo, String descripcion, Evento evento, 
            BigInteger semestre, Estudiante estudiante, Date fechaHora, Date fecha, EstadoPeticion idEstado);
   Boolean altaReclamoBasico(String titulo, String descripcion, Date fecha, Estudiante estudiante, EstadoPeticion idEstado);
   Reclamo buscarReclamo(Estudiante estudiante, Date fecha, String titulo);
   Boolean existeUserByDoc(Estudiante estudiante, Date fecha, String titulo);
}
