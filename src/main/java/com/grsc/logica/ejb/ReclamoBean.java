package com.grsc.logica.ejb;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.modelo.daos.ReclamoJpaController;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Reclamo;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.EstadoPeticion;
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
    public List<Reclamo> listaReclamosByUser(Estudiante idUsuario) {
        return controlador.findReclamoByUsuario(idUsuario);
    }
    
    @Override
    public Boolean altaReclamo(String titulo, String descripcion, Evento evento, 
            BigInteger semestre, Estudiante estudiante, Date fechaHora, Date fecha) {
        
        boolean pudeCrear = false;

        if ( existeReclamo(estudiante,titulo) ) {
            System.out.println("Reclamo ya registrado");
        } else {
            Reclamo reclamo = Reclamo.builder()
                    .titulo(titulo)
                    .detalle(descripcion)
                    .idEvento(evento)
                    .semestre(semestre)
                    .idUsuario(estudiante)
                    .fechaHora(fechaHora)
                    .fecha(fecha)
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
    public Reclamo buscarReclamo(Estudiante estudiante, String titulo) {
       return controlador.buscarReclamo(estudiante, titulo);
    }

    @Override
    public Boolean altaReclamoBasico(String titulo, String descripcion, Date fechaHora, Estudiante estudiante) {
    
        boolean pudeCrear = false;
        if ( existeReclamo(estudiante,titulo) ) {
            System.out.println("Reclamo ya registrado");
        } else {
            Reclamo reclamo = Reclamo.builder()
                    .titulo(titulo)
                    .detalle(descripcion)
                    .fechaHora(fechaHora)
                    .idUsuario(estudiante)
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
    public Boolean existeReclamo(Estudiante estudiante, String titulo) {
        Reclamo rec = buscarReclamo(estudiante,titulo);
        return rec.getIdReclamo()!=null;
        
    }
    
    @Override
    public Boolean borrarReclamo(BigInteger id){          
        boolean pudeEliminar = false;
        Reclamo reclamo = controlador.findReclamo(id);
        if ( reclamo!=null ) {
            try {
                controlador.destroy(id);
                pudeEliminar = true;
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ReclamoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("Reclamo con dicha ci no registrado");
            
        }
        return pudeEliminar;
    }
    
    @Override
    public EstadoPeticion obtenerEstado(BigInteger id) {
        Reclamo reclamo = controlador.findReclamo(id);
        return reclamo.getIdEstadoPeticion();
    }

    @Override
    public Reclamo buscarReclamoPorId(BigInteger id) {
         return controlador.findReclamo(id);
    }

    @Override
    public Boolean modificarEstado(Reclamo rec, EstadoPeticion estado, Date fechaHora)throws Exception{
        
        boolean pudeModificar = false;
            Reclamo reclamo = Reclamo.builder()
                    .idReclamo(rec.getIdReclamo())
                    .idUsuario(rec.getIdUsuario())
                    .idEvento(rec.getIdEvento())
                    .detalle(rec.getDetalle())
                    .titulo(rec.getTitulo())
                    .idEstadoPeticion(estado)
                    .fechaHora(fechaHora)
                    .build();

            try {
                controlador.edit(reclamo);
                pudeModificar = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeModificar;
        
    }
    
    @Override
    public Reclamo buscarReclamo(Date fechayHora, Evento evento, Estudiante user) {
        return controlador.findReclamoUsuarioHoraEvento(fechayHora, evento, user);
    }

    @Override
    public Boolean modificarReclamo(Reclamo rec) {
        boolean pudeModificar = false;
        
        if(controlador.findReclamo(rec.getIdReclamo())!=null){
           
            try {
                controlador.edit(rec);
                pudeModificar = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
            System.out.println("No existe tal reclamo");
        }
       
        return pudeModificar;
    }

}
