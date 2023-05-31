package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Itr;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ItrBeanRemote {
    
    List<Itr> listarItrs();
    //Boolean altaITR(Itr itr);
}
