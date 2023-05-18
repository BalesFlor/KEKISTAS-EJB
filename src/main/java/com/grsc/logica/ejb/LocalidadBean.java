package com.grsc.logica.ejb;

import com.grsc.modelo.daos.LocalidadJpaController;
import com.grsc.modelo.entities.Localidad;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LocalidadBean implements LocalidadBeanRemote {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private LocalidadJpaController controlador= new LocalidadJpaController(entityManagerFactory);
    
    @Override
    public List<Localidad> listarLocals() {
         return controlador.findLocalidadEntities();
    }

    @Override
    public List<Localidad> listarLocalsByDepto(BigInteger idDepto) {
        return controlador.findByDepartamento(idDepto);
    }
    
    @Override
    public int obtenerCantidadLocalidades(BigInteger idDepto) {
        List<Localidad> lista = controlador.findByDepartamento(idDepto);

        return lista.size();
    }

    @Override
    public String[] listarNomLocalsByDepto(BigInteger idDepto) {
        List<String> listaNomLocals= controlador.findNombreByDepartamento(idDepto);
        
        String[] listaNombres= new String[listaNomLocals.size()];
        
        for(int i=0;i<listaNomLocals.size();i++){
            listaNombres[i]=listaNomLocals.get(i);
        }
        
        return listaNombres;
    }
}
