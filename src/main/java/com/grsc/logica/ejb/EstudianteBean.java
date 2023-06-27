package com.grsc.logica.ejb;

import com.grsc.modelo.daos.EstudianteJpaController;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Generacion;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EstudianteBean implements EstudianteBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private EstudianteJpaController controlador= new EstudianteJpaController(entityManagerFactory);
    
    
    @Override
    public Boolean existeEstudiante(BigInteger idUser) {
        Boolean existe = false;
        Estudiante estudiante= controlador.findEstudiante(idUser);
        if(!(estudiante==null)){
            existe=true;
        }
        return existe;
    }

    @Override
    public Boolean eliminarEstudiante(BigInteger id){
        
        boolean pudeEliminar = false;
        Estudiante user =controlador.findEstudiante(id);
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
           System.out.println("Estudiante con dicha ci no registrado");
            
        }
        return pudeEliminar;

    }
   
    @Override
    public Estudiante buscarEstudiante(BigInteger id){
        return controlador.findEstudiante(id);
    } 
    
    @Override
    public Boolean ingresarEstudiante(BigInteger id, Generacion anio) {
        boolean pudeCrear = false;
        
        UsuarioBean userBean = new UsuarioBean();
        Usuarios usuario = userBean.buscarUsuario(id);

        if (existeEstudiante(id)) {
            System.out.println("Estudiante con dicha ci ya registrado");
        } else {
            Estudiante tutor = Estudiante.builder()
                    .idUsuario(id)
                    .anioGen(anio)
                    .usuarios(usuario)
                    .build();

            try {
                controlador.create(tutor);
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
    public List<Estudiante> listarEstudiantes() {
    return controlador.findEstudianteEntities();
    }
    
}
