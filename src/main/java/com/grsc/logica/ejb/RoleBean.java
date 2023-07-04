package com.grsc.logica.ejb;

import com.grsc.modelo.daos.RolesJpaController;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Roles;
import java.math.BigInteger;
import java.text.ParseException;
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

  @Override
     public Boolean altaRol(String nombre) 
        throws ParseException {
        boolean altaITR = false;

        if (existeRolByNombre(nombre)) {
            System.out.println("ITR con dicho ID ya registrado");
        } else {
            Roles rol = Roles.builder()
            .nombre(nombre)
            .build();
                
           
        
            try {
                controlador.create(rol);
                altaITR = true;
            } catch (Exception ex) {
                Logger.getLogger(ItrBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return altaITR;
    }

    public Boolean existeRolByID(BigInteger idRol) {
    Boolean existe = false;
    Roles rol = controlador.findRoles(idRol);
    if (rol != null) {
        existe = true;
    }
    return existe;

    }
    public Boolean existeRolByNombre(String nombre) {
        Boolean existe = false;

       Roles rol = controlador.findRol(nombre);
        if (rol != null) {
            existe = true;
        }

        return existe;
    }


    
}

