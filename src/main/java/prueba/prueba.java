/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package prueba;

import com.grsc.logica.ejb.UsuarioBean;
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
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author fbale
 */
public class prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       /*
        Date fecha = new Date();
        BigInteger bi = new BigInteger("1");
        Usuarios user = new Usuarios(bi, "florBales", "Balestena",
                "Florencia", fecha, "23456789", 'F',
                "fbalestena@gmail.com", "099977583", "54269074");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
        
        DepartamentoJpaController deptoJpaController= new DepartamentoJpaController(entityManagerFactory);
        Departamento depto = deptoJpaController.findDepartamento(bi);
        user.setIdDepartamento(depto);

        ItrJpaController itrJpaController= new ItrJpaController(entityManagerFactory);
        Itr itr = itrJpaController.findItr(bi);
        user.setIdItr(itr);

        LocalidadJpaController localidadJpaController= new LocalidadJpaController(entityManagerFactory);
        Localidad localidad = localidadJpaController.findLocalidad(bi);
        user.setIdLocalidad(localidad);

        RolesJpaController rolesJpaController= new RolesJpaController(entityManagerFactory);
        Roles rol = rolesJpaController.findRoles(bi);
        user.setIdRol(rol);

        UsuariosJpaController userJpaController= new UsuariosJpaController(entityManagerFactory);
        
        try {
            userJpaController.create(user);
        } catch (Exception ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        BigInteger bi = new BigInteger("1");
       UsuarioBean beanUser= new UsuarioBean();
       Usuarios user1=beanUser.buscarUsuario(bi);
       
       System.out.println(user1);
       
    }
    
}
