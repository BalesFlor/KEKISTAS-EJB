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


public class AccionJustificacionesBean implements AccionJustificacionBeanRemote {
     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private AccionJustificacionJpaController controlador= new AccionJustificacionJpaController(entityManagerFactory);

    @Override
    public Boolean registrarAccion(Justificacion justificacion, Analista analista, String detalle, Date fechaHora) {
        
        boolean pudeCrear = false;
        AccionJustificacionPK accRecPK = new AccionJustificacionPK(justificacion.getIdJustificacion(), analista.getIdUsuario());
        
            AccionJustificacion accRec = AccionJustificacion.builder()
                    .accionJustificacionPK(accRecPK)
                    .justificacion(justificacion)
                    .analista(analista)
                    .detalle(detalle)
                    .fechaHora(fechaHora)
                    .build();

            try {
                controlador.create(accRec);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeCrear;
    }

    @Override
    public AccionJustificacion buscarAccionJustificacion(Analista analista, Date fechaHora){
        return controlador.findAccionJustificacion(analista, fechaHora);
    }
    
}
