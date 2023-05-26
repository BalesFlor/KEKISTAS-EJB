/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grsc.logica.ejb;

import com.grsc.modelo.daos.ReclamoJpaController;
import com.grsc.modelo.entities.Reclamo;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author fbale
 */
public class ReclamoBean implements ReclamoBeanRemote {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private ReclamoJpaController controlador= new ReclamoJpaController(entityManagerFactory);
    
    @Override
    public List<Reclamo> listaTodosReclamos() {
        return controlador.findReclamoEntities();
    }

    
}
