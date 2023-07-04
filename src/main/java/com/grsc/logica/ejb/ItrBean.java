package com.grsc.logica.ejb;

import com.grsc.modelo.daos.ItrJpaController;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ItrBean implements ItrBeanRemote {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private ItrJpaController controlador = new ItrJpaController(entityManagerFactory);

    @Override
    public List<Itr> listarItrs() {
        return controlador.findItrEntities();
    }

    @Override
    public Boolean altaITR(String nomItr, Departamento departamento) 
        throws ParseException {
        boolean altaITR = false;

        if (existeItrByNombre(nomItr)) {
            System.out.println("ITR con dicho ID ya registrado");
        } else {
            Itr itr = Itr.builder()
            .nomItr(nomItr)
            .idDepartamento(departamento)
            .build();
                
           
        
            try {
                controlador.create(itr);
                altaITR = true;
            } catch (Exception ex) {
                Logger.getLogger(ItrBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return altaITR;
    }
    

    
    public Boolean modificarITR(Itr itr) {
        boolean pudeModificar = false;

        try {
            controlador.edit(itr);
            pudeModificar = true;
        } catch (Exception ex) {
            Logger.getLogger(ItrBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pudeModificar;
    }

   
    public Boolean eliminarITR(BigInteger idITR) {
        boolean pudeEliminar = false;

        try {
            controlador.destroy(idITR);
            pudeEliminar = true;
        } catch (Exception ex) {
            Logger.getLogger(ItrBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pudeEliminar;
    }

    public Boolean existeItrByID(BigInteger idITR) {
        Boolean existe = false;

        Itr itr = controlador.findItr(idITR);
        if (itr != null) {
            existe = true;
        }

        return existe;
    }
public Boolean existeItrByNombre(String nombre) {
        Boolean existe = false;

        Itr itr = controlador.findItr(nombre);
        if (itr != null) {
            existe = true;
        }

        return existe;
    }


   
}

