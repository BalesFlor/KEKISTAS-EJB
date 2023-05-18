package com.grsc.logica.ejb;

import com.grsc.modelo.daos.GeneracionJpaController;
import com.grsc.modelo.entities.Generacion;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GeneracionBean implements GeneracionBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private GeneracionJpaController controlador= new GeneracionJpaController(entityManagerFactory);
    
    
    @Override
    public Generacion buscarGen(BigInteger anio) {
        return controlador.findByAnio(anio);
    }
    
    @Override
    public List<Generacion> listarGeneracion() {
        return controlador.findGeneracionEntities();
    }
}
