package com.grsc.logica.ejb;

import com.grsc.modelo.daos.RolesJpaController;
import com.grsc.modelo.entities.Roles;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Stateless
public class RoleBean implements RoleBeanRemote {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private RolesJpaController controlador= new RolesJpaController(entityManagerFactory);

    public RoleBean() {
    }

    @Override
    public List<Roles> listarRoles() {
        return controlador.findRolesEntities();
    }
    
}
