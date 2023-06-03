package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Area;
import com.grsc.modelo.entities.TipoTutor;
import com.grsc.modelo.entities.Tutor;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DocenteBeanRemote {
    Boolean existeTutor(BigInteger idUser);
    Boolean ingresarDocente(BigInteger id,TipoTutor tipo, Area area);
    Boolean eliminarDocente(BigInteger idUser);
    List<Tutor> listarDocentes();
}
