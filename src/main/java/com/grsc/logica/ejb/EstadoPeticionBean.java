/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EstadoPeticionJpaController;
import com.grsc.modelo.entities.EstadoPeticion;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author fbale
 */
public class EstadoPeticionBean implements EstadoPeticionBeanRemote{
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EstadoPeticionJpaController controlador= new EstadoPeticionJpaController(entityManagerFactory);
    
    @Override
    public List<EstadoPeticion> listarEstados() {
        return controlador.findEstadoPeticionEntities();
    }
    
    
}
