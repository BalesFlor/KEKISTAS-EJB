package com.grsc.logica.ejb;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.modelo.daos.JustificacionJpaController;
import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JustificacionBean implements JustificacionBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private JustificacionJpaController controlador= new JustificacionJpaController(entityManagerFactory);
    
    @Override
    public Boolean hacerJustificacion(Date fechaHora, String detalle, Evento evento, Estudiante usuario) throws ParseException {
        
        boolean pudeCrear = false;

        if ( existeJustificacion(detalle) ) {
            System.out.println("Justificacion con dicha documento ya registrado");
        } else {
            Justificacion jus = Justificacion.builder()
                            .idEvento(evento)
                            .idUsuario(usuario)
                            .fechaHora(fechaHora)
                            .detalle(detalle)
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
    public Justificacion buscarJustificacion(String detalle) {
        return controlador.findJustificacionUsuarioHoraEvento(detalle);
    }

    @Override
    public Boolean existeJustificacion(String detalle) {
        Justificacion jus = buscarJustificacion(detalle);
        return jus.getIdJustificacion()!=null;
    }
    
    @Override
    public Boolean borrarJustificacion(BigInteger id){          
        boolean pudeEliminar = false;
        Justificacion justificacion = controlador.findJustificacion(id);
        if ( justificacion!=null ) {
            try {
                controlador.destroy(id);
                pudeEliminar = true;
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(JustificacionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("Justificacion con dicha ci no registrado");
            
        }
        return pudeEliminar;
    }

    @Override
    public Boolean modificarJustificacion(BigInteger id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public Justificacion buscarJustificacionPorId(BigInteger id) {
         return controlador.findJustificacion(id);
    }

    @Override
    public Boolean modificarEstado(Justificacion jus, EstadoPeticion estado, Date fechaHora)throws Exception{
        
        boolean pudeModificar = false;
            Justificacion justificacion = Justificacion.builder()
                    .idJustificacion(jus.getIdJustificacion())
                    .idUsuario(jus.getIdUsuario())
                    .idEvento(jus.getIdEvento())
                    .detalle(jus.getDetalle())
                    .idEstadoPeticion(estado)
                    .fechaHora(fechaHora)
                    .build();
            try {
                controlador.edit(justificacion);
                pudeModificar = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeModificar;
        
    }

    @Override
    public List<Justificacion> listarJustificacions() {
        return controlador.findJustificacionEntities();
    }

    
}
