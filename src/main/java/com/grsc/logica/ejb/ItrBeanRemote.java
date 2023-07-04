package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ItrBeanRemote {
    
    List<Itr> listarItrs();
    Boolean altaITR(String nomItr, Departamento departamento) throws ParseException;
}
