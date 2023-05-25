package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EstadoUsuarioJpaController;
import com.grsc.modelo.entities.EstadoUsuario;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EstadoUsuarioBean implements EstadoUsuarioBeanRemote{
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EstadoUsuarioJpaController controlador= new EstadoUsuarioJpaController(entityManagerFactory);

    @Override
    public List<EstadoUsuario> listarEstadosUsuario() {
        return controlador.findEstadoUsuarioEntities();
    }
    
    @Override
    public EstadoUsuario buscar(BigInteger id) {
        return controlador.findEstadoUsuario(id);
    }
    
}
