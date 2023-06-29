package com.grsc.logica.ejb;

import com.grsc.modelo.daos.ReclamoJpaController;
import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Reclamo;
import com.grsc.modelo.entities.Evento;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ReclamoBean implements ReclamoBeanRemote {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private ReclamoJpaController controlador= new ReclamoJpaController(entityManagerFactory);
    
    @Override
    public List<Reclamo> listaTodosReclamos() {
        return controlador.findReclamoEntities();
    }

    @Override
    public Boolean altaReclamo(String titulo, String descripcion, Evento evento, 
            BigInteger semestre, Estudiante estudiante, Date fechaHora, Date fecha, EstadoPeticion idEstado) {
        
        boolean pudeCrear = false;

        if ( existeUserByDoc(estudiante,fecha,titulo)) {
            System.out.println("Reclamo ya registrado");
        } else {
            Reclamo reclamo = Reclamo.builder()
                    .titulo(titulo)
                    .detalle(descripcion)
                    .idEvento(evento)
                    .semestre(semestre)
                    .idUsuario(estudiante)
                    .fechaHora(fecha)
                    .fecha(fechaHora)
                    .idEstadoPeticion(idEstado)
                    .build();

            try {
                controlador.create(reclamo);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pudeCrear;
    }

    @Override
    public Reclamo buscarReclamo(Estudiante estudiante, Date fecha, String titulo) {
       return controlador.buscarReclamo(estudiante, fecha, titulo);
    }

    @Override
    public Boolean altaReclamoBasico(String titulo, String descripcion, Date fecha, Estudiante estudiante, EstadoPeticion idEstado) {
    
        boolean pudeCrear = false;

            Reclamo reclamo = Reclamo.builder()
                    .titulo(titulo)
                    .detalle(descripcion)
                    .fechaHora(fecha)
                    .idUsuario(estudiante)
                    .idEstadoPeticion(idEstado)
                    .build();

            try {
                controlador.create(reclamo);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        return pudeCrear;
    }

    @Override
    public Boolean existeUserByDoc(Estudiante estudiante, Date fecha, String titulo) {
        Boolean existe = false;
        Reclamo rec= controlador.buscarReclamo(estudiante,fecha,titulo);
        if(!(rec==null)){
            existe=true;
        }
        return existe;
    }
}
