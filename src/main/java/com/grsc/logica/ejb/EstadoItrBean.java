package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EstadoItrJpaController;
import com.grsc.modelo.entities.EstadoItr;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EstadoItrBean implements EstadoItrBeanRemote {
     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EstadoItrJpaController controlador= new EstadoItrJpaController(entityManagerFactory);
    
    @Override
    public List<EstadoItr> listarEstados() {
        return controlador.findEstadoItrEntities();
    }
    
    @Override
    public EstadoItr buscar(BigInteger id) {
        return controlador.findEstadoItr(id);
    }

    @Override
    public EstadoItr buscarPorNom(String nom){
        return controlador.findEstadoItr(nom);
    }
}
