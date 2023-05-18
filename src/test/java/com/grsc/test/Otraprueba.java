package com.grsc.test;

import com.grsc.modelo.daos.DepartamentoJpaController;
import com.grsc.modelo.daos.ItrJpaController;
import com.grsc.modelo.daos.LocalidadJpaController;
import com.grsc.modelo.daos.RolesJpaController;
import com.grsc.modelo.daos.UsuariosJpaController;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Localidad;
import com.grsc.modelo.entities.Roles;
import com.grsc.modelo.entities.Usuarios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fbale
 */
public class Otraprueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
    }
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        Date fecha = new Date();

        Usuarios user = new Usuarios(1L, "florBales", "Balestena",
                "Florencia", fecha, "123456789", 'F',
                "fbalestena@gmail.com", "099977583", "54269074");
        
        DepartamentoJpaController deptoJpaController= new DepartamentoJpaController(entityManagerFactory);
        Departamento depto = deptoJpaController.findDepartamento(1L);
        user.setDepartamento(depto);

        ItrJpaController itrJpaController= new ItrJpaController(entityManagerFactory);
        Itr itr = itrJpaController.findItr(1L);
        user.setItr(itr);

        LocalidadJpaController localidadJpaController= new LocalidadJpaController(entityManagerFactory);
        Localidad localidad = localidadJpaController.findLocalidad(1L);
        user.setLocalidad(localidad);

        RolesJpaController rolesJpaController= new RolesJpaController(entityManagerFactory);
        Roles rol = rolesJpaController.findRoles(1L);
        user.setRol(rol);

        UsuariosJpaController userJpaController= new UsuariosJpaController(entityManagerFactory);
        
        try {
            userJpaController.create(user);
        } catch (Exception ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        System.out.println("TESTEO DEL PROYECTO BASE CORRECTO !");
        
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
       
        */
    }
}
