package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Usuarios;
import javax.ejb.Stateless;
import com.grsc.modelo.daos.UsuariosJpaController;
import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.EstadoUsuario;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Localidad;
import com.grsc.modelo.entities.Roles;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Stateless
public class UsuarioBean implements UsuarioBeanRemote {
    
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private UsuariosJpaController controlador= new UsuariosJpaController(entityManagerFactory);
    
    public UsuarioBean() {
    }

    @Override
    public String imprimirUsuario(Usuarios user) {
        return "Nombre: " + user.getNomUsuario() + " Telefono: " + user.getTelefono();
    }

    @Override
    public Usuarios buscarUsuario(BigInteger id) {
        Usuarios user = controlador.findUsuarios(id);
        return user;
    }

    @Override
    public Usuarios buscarUsuarioPorDocumento(String documento) {
        Usuarios user = controlador.findByDocumento(documento);
        return user;
    }
    
    @Override
    public Boolean validarLogin(String nomUser, String password) {
        boolean existe = false;

        Usuarios user = controlador.usuarioLogin(nomUser, password);

        if (user == null) {
            existe = false;
        } else {
            existe = true;
        }

        return existe;
    }

    @Override
    public Usuarios buscarUserByNombre(String nomUser){
        return controlador.findUsuarios(nomUser);
    }
    
    @Override
    public Usuarios usuarioLogeado(String nomUser, String password) {
        return controlador.usuarioLogin(nomUser, password);
    }

    @Override
    public Roles mostrarRolUsuario(BigInteger id) {
        Usuarios user=controlador.findUsuarios(id);
        return user.getRol();
    }

    @Override
    public Boolean existeUserByDoc(String documento) {
        Boolean existe = false;
        Usuarios user= controlador.findByDocumento(documento);
        if(!(user==null)){
            existe=true;
        }
        return existe;
    }

    @Override
    public Boolean registrarUsuario(String nomUsuario, String apellido1, String nombre2, String nombre1,
            Date fecNac, String contrasenia, Character genero, String mailInstitucional, String telefono, 
            String documento, String apellido2, String mailPersonal, EstadoUsuario idEstadoUsuario, 
             Departamento departamento, Itr itr, Localidad localidad, Roles rol) 
            throws ParseException {

        boolean pudeCrear = false;

        if (existeUserByDoc(documento)) {
            System.out.println("Usuario con dicha documento ya registrado");
        } else {
            Usuarios user = Usuarios.builder()
                    .nomUsuario(nomUsuario)
                    .nombre1(nombre1)
                    .nombre2(nombre2)
                    .apellido1(apellido1)
                    .apellido2(apellido2)
                    .documento(documento)
                    .telefono(telefono)
                    .mailInstitucional(mailInstitucional)
                    .mailPersonal(mailPersonal)
                    .genero(genero)
                    .fecNac(fecNac)
                    .Itr(itr)
                    .departamento(departamento)
                    .Localidad(localidad)
                    .contrasenia(contrasenia)
                    .Rol(rol)
                    .build();

            try {
                controlador.create(user);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pudeCrear;
    }

    @Override
    public Usuarios obtenerPorCI(String documento) {
        return controlador.findByDocumento(documento);
    }

    @Override
    public Boolean eliminarUser(BigInteger id) {
        
        boolean pudeEliminar = false;
        Usuarios user =controlador.findUsuarios(id);
        if (!(user==null)) {
            try {
                controlador.destroy(id);
                pudeEliminar = true;
            } catch (IllegalOrphanException ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           System.out.println("Usuario con dicha ci no registrado");
            
        }
        return pudeEliminar;

    }

    @Override
    public Boolean existeUserByTel(String telefono) {
        Boolean existe = false;
        Usuarios user= controlador.findByTelefono(telefono);
        if(!(user==null)){
            existe=true;
        }
        return existe;
    }

    @Override
    public Boolean existeUserByMailC(String mailInsti) {
        Boolean existe = false;
        Usuarios user= controlador.findByMailC(mailInsti);
        if(!(user==null)){
            existe=true;
        }
        return existe;
    }

    @Override
    public Boolean existeUserByMailP(String mailPers) {
        Boolean existe = false;
        Usuarios user = controlador.findByMailP(mailPers);
        if (!(user == null)) {
            existe = true;
        }
        return existe;
    }

    @Override
    public List<Usuarios> listarUsuarios() {        
        return controlador.findUsuariosEntities();
    }
    
    @Override
    public EstadoUsuario obtenerEstado(BigInteger id) {
        Usuarios user = controlador.findUsuarios(id);
        return user.getIdEstadoUsuario();
    }
}
