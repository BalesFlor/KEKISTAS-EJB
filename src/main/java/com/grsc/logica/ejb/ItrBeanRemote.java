package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.EstadoItr;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ItrBeanRemote {
    
    List<Itr> listarItrs();
    Boolean altaITR(String nomItr, Departamento departamento, EstadoItr estado) throws ParseException;
    Itr buscarItr(String nom);
    Boolean modificarITR(Itr itr);
    Boolean eliminarITR(BigInteger idITR);
}
