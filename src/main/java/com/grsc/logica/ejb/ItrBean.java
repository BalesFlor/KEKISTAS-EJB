package com.grsc.logica.ejb;

import com.grsc.modelo.daos.ItrJpaController;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ItrBean implements ItrBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private ItrJpaController controlador= new ItrJpaController(entityManagerFactory);
    
    
    @Override
    public List<Itr> listarItrs() {
        return controlador.findItrEntities();
    }
/*
    @Override
    public Boolean altaITR(Itr itr){

        boolean pudeCrear = false;

        if (existeItrByID(itr.getIdItr())) {
            System.out.println("Usuario con dicha documento ya registrado");
        } else {
            Itr itr = Itr.builder()
                    .idDepartamento(itr.getIdDepartamento())
                    .nomItr(itr.getNomItr())
                    .build();

            try {
                controlador.create(itr);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pudeCrear;
    }
    }

    public Boolean existeItrByID(BigInteger idITR) {
        Boolean existe = false;
        /*
        Itr itr= controlador.findByIDItr(itr.getID());
        if(!(itr==null)){
            existe=true;
        }
    
        return existe;
    }
*/
}
