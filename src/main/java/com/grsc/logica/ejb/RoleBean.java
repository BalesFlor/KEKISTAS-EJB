package com.grsc.logica.ejb;

import com.grsc.modelo.daos.RolesJpaController;
import com.grsc.modelo.entities.Roles;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

  
    public Boolean altaRol(Roles rol) {
    boolean pudeCrear = false;

    if (existeRolByID(rol.getIdRol())) {
        System.out.println("Rol con dicho ID ya existe");
    } else {
        try {
            controlador.create(rol);
            pudeCrear = true;
        } catch (Exception ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return pudeCrear;
}

    public Boolean existeRolByID(BigInteger idRol) {
    Boolean existe = false;
    Roles rol = controlador.findRoles(idRol);
    if (rol != null) {
        existe = true;
    }
    return existe;

    }
    
}

