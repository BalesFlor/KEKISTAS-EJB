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
            BigInteger semestre, Estudiante estudiante, Date fechaHora, Date fecha);
   Boolean altaReclamoBasico(String titulo, String descripcion, Date fecha, Estudiante estudiante);
   Reclamo buscarReclamo(Estudiante estudiante, String titulo);
   Boolean existeReclamo(Estudiante estudiante, String titulo);
   Boolean borrarReclamo(BigInteger id);
   EstadoPeticion obtenerEstado(BigInteger id);
   Boolean modificarEstado(Reclamo rec, EstadoPeticion estado, Date fechaHora)throws Exception;
   Reclamo buscarReclamoPorId(BigInteger id);
   Reclamo buscarReclamo(Date fechayHora, Evento evento, Estudiante user);
   Boolean modificarReclamo(Reclamo rec);
   List<Reclamo> listaReclamosByUser(Estudiante idUsuario);
}
