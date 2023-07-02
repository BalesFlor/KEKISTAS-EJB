package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.EstadoUsuario;
import com.grsc.modelo.entities.Localidad;
import com.grsc.modelo.entities.Roles;
import javax.ejb.Remote;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Remote
public interface UsuarioBeanRemote {
   
    Boolean registrarUsuario(String nomUsuario, String apellido1, String nombre2, String nombre1,
            Date fecNac, String contrasenia, Character genero, String mailInstitucional, String telefono, 
            String documento, String apellido2, String mailPersonal, EstadoUsuario idEstadoUsuario, 
            Departamento departamento, Itr itr, Localidad localidad, Roles rol) throws ParseException ;
    String imprimirUsuario(Usuarios user);
    Usuarios buscarUsuario(BigInteger id);
    Usuarios buscarUserByNombre(String nomUser);
    Usuarios buscarUsuarioPorDocumento(String documento);
    Boolean validarLogin(String nomUser, String password);
    Usuarios usuarioLogeado(String nomUser, String password);
    Roles mostrarRolUsuario(BigInteger id);
    Usuarios obtenerPorCI(String documento);
    Boolean existeUserByDoc(String documento);
    Boolean eliminarUser(BigInteger id);
    Boolean existeUserByTel(String telefono);
    Boolean existeUserByMailC(String mailConsti);
    Boolean existeUserByMailP(String mailPers);
    List<Usuarios> listarUsuarios();
    EstadoUsuario obtenerEstado(BigInteger id);
}
