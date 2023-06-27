package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Generacion;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface EstudianteBeanRemote {
    Boolean existeEstudiante(BigInteger idUser);
    Estudiante buscarEstudiante(BigInteger idUser);
    Boolean eliminarEstudiante(BigInteger idUser);
    Boolean ingresarEstudiante(BigInteger id,Generacion anio);
    List<Estudiante> listarEstudiantes();
}
