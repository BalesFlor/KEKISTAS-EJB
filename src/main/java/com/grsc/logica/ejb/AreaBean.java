package com.grsc.logica.ejb;

import com.grsc.modelo.daos.AreaJpaController;
import com.grsc.modelo.entities.Area;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AreaBean implements AreaBeanRemote{
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private AreaJpaController controlador= new AreaJpaController(entityManagerFactory);

    @Override
    public List<Area> listarArea() {
        return controlador.findAreaEntities();
    }
    
    
}
