package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Departamento;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.grsc.modelo.daos.DepartamentoJpaController;
import java.math.BigInteger;

public class DepartamentoBean implements DepartamentoBeanRemote{
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private DepartamentoJpaController controlador= new DepartamentoJpaController(entityManagerFactory);
    
    @Override
    public List<Departamento> listarDepartamento() {
        return controlador.findDepartamentoEntities();
    }

    @Override
    public Departamento buscarDepto(BigInteger id) {
        return controlador.findDepartamento(id);
    }
}
