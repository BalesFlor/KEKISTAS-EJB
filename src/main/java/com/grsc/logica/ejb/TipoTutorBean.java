package com.grsc.logica.ejb;

import com.grsc.modelo.daos.TipoTutorJpaController;
import com.grsc.modelo.entities.TipoTutor;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TipoTutorBean implements TipoTutorBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private TipoTutorJpaController controlador= new TipoTutorJpaController(entityManagerFactory);
    
    @Override
    public List<TipoTutor> listarTipoTutor() {
        return controlador.findTipoTutorEntities();
    }
    
}
