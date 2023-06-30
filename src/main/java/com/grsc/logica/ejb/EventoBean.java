package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EventoJpaController;
import com.grsc.modelo.entities.Evento;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EventoBean implements EventoBeanRemote {

     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EventoJpaController controlador= new EventoJpaController(entityManagerFactory);
    
    
    @Override
    public Boolean hacerEvento(Date fechaHoraInicio, Date fechaHoraFin, String titulo) throws ParseException {
       
        boolean pudeCrear = false;

        if ( buscarEvento(fechaHoraInicio, titulo) != null  ) {
            System.out.println("Evento con dicha documento ya registrado");
        } else {
            Evento jus = Evento.builder()
                    .fechaHoraInicio(fechaHoraInicio)
                    .fechaHoraFin(fechaHoraFin)
                    .titulo(titulo)
                    .build();

            try {
                controlador.create(jus);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pudeCrear;
    }

    @Override
    public Evento buscarEvento(Date fechaHoraInicio, String titulo) {
        return controlador.findEvento(fechaHoraInicio, titulo);
    }

   @Override
    public Evento buscarEvento( String titulo) {
        return controlador.findEvento(titulo);
    }

    
    @Override
    public Evento buscarEventoPorId(BigInteger id) {
        return controlador.findEvento(id);
    }
    
    @Override
    public Boolean eliminarEvento(BigInteger id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean modificarEvento(BigInteger id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Evento> listarEventos() {
        return controlador.findEventoEntities();
    }
    
}
