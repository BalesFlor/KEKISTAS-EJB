package com.grsc.logica.ejb;

import com.grsc.modelo.daos.AccionJustificacionJpaController;
import com.grsc.modelo.entities.AccionJustificacion;
import com.grsc.modelo.entities.AccionJustificacionPK;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Justificacion;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class AccionJustificacionBean implements AccionJustificacionBeanRemote {
     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private AccionJustificacionJpaController controlador= new AccionJustificacionJpaController(entityManagerFactory);

    @Override
    public Boolean registrarAccion(Justificacion justificacion, Analista analista, String detalle, Date fechaHora) {
        
        boolean pudeCrear = false;
        AccionJustificacionPK accJusPK = new AccionJustificacionPK(justificacion.getIdJustificacion(), analista.getIdUsuario());
        
            AccionJustificacion accJus = AccionJustificacion.builder()
                    .accionJustificacionPK(accJusPK)
                    .justificacion(justificacion)
                    .analista(analista)
                    .detalle(detalle)
                    .fechaHora(fechaHora)
                    .build();

            try {
                controlador.create(accJus);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeCrear;
    }

    
    @Override
    public AccionJustificacion buscarAccionJustificacion(Justificacion justificacion, Analista analista){
        return controlador.findAccionJustificacion(justificacion, analista);
    }
    
    @Override
    public Boolean modificarAccion(Justificacion justificacion, Analista analista, String detalle, Date fechaHora){
    
        boolean pudeModificar = false;
        AccionJustificacionPK accJusPK = new AccionJustificacionPK(justificacion.getIdJustificacion(), analista.getIdUsuario());
        
            AccionJustificacion accJus = AccionJustificacion.builder()
                    .accionJustificacionPK(accJusPK)
                    .justificacion(justificacion)
                    .analista(analista)
                    .detalle(detalle)
                    .fechaHora(fechaHora)
                    .build();

            try {
                controlador.edit(accJus);
                pudeModificar = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeModificar;
        
    }
}
