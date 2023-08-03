package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface JustificacionBeanRemote {
    Boolean hacerJustificacion(Date fechaHora, String detalle, Evento idEvento, Estudiante idUsuario) throws ParseException ;
    Justificacion buscarJustificacion(String detalle);
    Boolean existeJustificacion(String detalle);
    Boolean modificarJustificacion(BigInteger id);
    List<Justificacion> listarJustificacions();
    Boolean borrarJustificacion(BigInteger id);
    Boolean modificarEstado(Justificacion jus, EstadoPeticion estado, Date fechaHora)throws Exception;
    Justificacion buscarJustificacionPorId(BigInteger id);
    Boolean modificarJustificacion(Justificacion jus);
    List<Justificacion> listaJustificacionsByUser(Estudiante idUsuario);
}
