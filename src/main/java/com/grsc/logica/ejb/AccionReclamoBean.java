package com.grsc.logica.ejb;

import com.grsc.modelo.daos.AccionReclamoJpaController;
import com.grsc.modelo.entities.AccionReclamo;
import com.grsc.modelo.entities.AccionReclamoPK;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Reclamo;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AccionReclamoBean implements AccionReclamoBeanRemote {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private AccionReclamoJpaController controlador= new AccionReclamoJpaController(entityManagerFactory);
    
    @Override
    public Boolean registrarAccion(Reclamo reclamo, Analista analista, String detalle, Date fechaHora) {
        
        boolean pudeCrear = false;
        AccionReclamoPK accRecPK = new AccionReclamoPK(reclamo.getIdReclamo(), analista.getIdUsuario());
        
            AccionReclamo accRec = AccionReclamo.builder()
                    .accionReclamoPK(accRecPK)
                    .reclamo(reclamo)
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
    public AccionReclamo buscarAccionReclamo(Reclamo reclamo, Analista analista){
        return controlador.findAccionReclamo(reclamo, analista);
    }
    
    @Override
    public Boolean modificarAccion(Reclamo reclamo, Analista analista, String detalle, Date fechaHora){
    
        boolean pudeModificar = false;
        AccionReclamoPK accRecPK = new AccionReclamoPK(reclamo.getIdReclamo(), analista.getIdUsuario());
        
            AccionReclamo accRec = AccionReclamo.builder()
                    .accionReclamoPK(accRecPK)
                    .reclamo(reclamo)
                    .analista(analista)
                    .detalle(detalle)
                    .fechaHora(fechaHora)
                    .build();

            try {
                controlador.edit(accRec);
                pudeModificar = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        return pudeModificar;
        
    }
}
