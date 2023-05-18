
package com.grsc.logica.ejb;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.modelo.daos.AnalistaJpaController;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.hibernate.criterion.Projections.id;

/**
 *
 * @author fbale
 */
public class AnalistaBean implements AnalistaBeanRemote{
     EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private AnalistaJpaController controlador= new AnalistaJpaController(entityManagerFactory);
    
    @Override
    public Boolean existeAnalista(BigInteger idUser) {
        Boolean existe = false;
        Analista analista= controlador.findAnalista(idUser);
        if(!(analista==null)){
            existe=true;
        }
        return existe;
    }
    
    @Override
    public Boolean ingresarAnalista(BigInteger id) {
       boolean pudeCrear = false;
       
       UsuarioBean userBean= new UsuarioBean();
       Usuarios usuario= userBean.buscarUsuario(id);
       
        if (existeAnalista(id)) {
            System.out.println("Analista con dicha ci ya registrado");
        } else {
            Analista analista = Analista.builder()
                    .idUsuario(id)
                    .usuarios(usuario)
                    .build();
            System.out.println(analista);
            try {
                controlador.create(analista);
                pudeCrear = true;
            } catch (PreexistingEntityException ex) {
                Logger.getLogger(DocenteBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) { 
               Logger.getLogger(DocenteBean.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        return pudeCrear;
    }
    
    @Override
    public Boolean eliminarAnalista(BigInteger id){
        
        boolean pudeEliminar = false;
        Analista user =controlador.findAnalista(id);
        if (!(user==null)) {
            try {
                controlador.destroy(id);
                pudeEliminar = true;
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("Analista con dicha ci no registrado");
            
        }
        return pudeEliminar;

    }
    
}