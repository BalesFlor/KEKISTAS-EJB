package com.grsc.logica.ejb;

import com.grsc.modelo.entities.TipoTutor;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface TipoTutorBeanRemote {
    List<TipoTutor> listarTipoTutor();
}
