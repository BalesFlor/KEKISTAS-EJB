package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EstadoPeticionJpaController;
import com.grsc.modelo.entities.EstadoPeticion;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EstadoPeticionBean implements EstadoPeticionBeanRemote{
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EstadoPeticionJpaController controlador= new EstadoPeticionJpaController(entityManagerFactory);
    
    @Override
    public List<EstadoPeticion> listarEstados() {
        return controlador.findEstadoPeticionEntities();
    }
    
    @Override
    public EstadoPeticion buscar(BigInteger id) {
        return controlador.findEstadoPeticion(id);
    }

    @Override
    public EstadoPeticion buscarPorNom(String nom){
        return controlador.findEstadoPeticion(nom);
    }
}
