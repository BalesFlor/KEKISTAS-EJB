package com.grsc.logica.ejb;

import com.grsc.modelo.daos.TipoEventoJpaController;
import com.grsc.modelo.entities.TipoEvento;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TipoEventoBean implements TipoEventoBeanRemote{
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private TipoEventoJpaController controlador= new TipoEventoJpaController(entityManagerFactory);
    
    @Override
    public List<TipoEvento> listaTipos(){
       return controlador.findTipoEventoEntities();
    }
    
}
