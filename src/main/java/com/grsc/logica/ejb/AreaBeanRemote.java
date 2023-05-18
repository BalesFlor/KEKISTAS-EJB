package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Area;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AreaBeanRemote {
    
    List<Area> listarArea();
    
}
