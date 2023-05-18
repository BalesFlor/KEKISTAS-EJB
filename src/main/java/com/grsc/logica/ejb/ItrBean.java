package com.grsc.logica.ejb;

import com.grsc.modelo.daos.ItrJpaController;
import com.grsc.modelo.entities.Itr;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ItrBean implements ItrBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private ItrJpaController controlador= new ItrJpaController(entityManagerFactory);
    
    
    @Override
    public List<Itr> listarItrs() {
        return controlador.findItrEntities();
    }
    
}
