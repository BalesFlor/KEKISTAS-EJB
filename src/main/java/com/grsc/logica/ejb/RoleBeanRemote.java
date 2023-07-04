package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Roles;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface RoleBeanRemote {
    
    List<Roles> listarRoles();
    Boolean altaRol(String nombre) throws ParseException;
}
