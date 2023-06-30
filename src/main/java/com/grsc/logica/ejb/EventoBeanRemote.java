package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Evento;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface EventoBeanRemote {
    Boolean hacerEvento(Date fechaHoraInicio, Date fechaHoraFin, String titulo) throws ParseException ;
    Evento buscarEvento(Date fechaHoraInicio, String titulo);
     Evento buscarEvento(String titulo);
    Evento buscarEventoPorId(BigInteger id);
    Boolean eliminarEvento(BigInteger id);
    Boolean modificarEvento(BigInteger id);
    List<Evento> listarEventos();
}

