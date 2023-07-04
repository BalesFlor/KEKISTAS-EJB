package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Departamento;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DepartamentoBeanRemote {
    List<Departamento> listarDepartamento();
    Departamento buscarDeptobyNombre(String nombre);
    Departamento buscarDepto(BigInteger id);
}
