package com.grsc.logica.ejb;

import com.grsc.modelo.entities.TipoEvento;
import java.util.List;

public interface TipoEventoBeanRemote {
    List<TipoEvento> listaTipos();
}
