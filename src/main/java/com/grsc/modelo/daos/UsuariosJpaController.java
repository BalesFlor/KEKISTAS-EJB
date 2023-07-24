package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.logica.ejb.EstadoUsuarioBean;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Tutor;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.EstadoUsuario;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Localidad;
import com.grsc.modelo.entities.Roles;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(usuarios);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuarios = em.merge(usuarios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            
            BigInteger idEstadoEliminado= BigInteger.valueOf(3L);
            EstadoUsuarioBean estadoBean= new EstadoUsuarioBean();
            EstadoUsuario estadoEliminado=estadoBean.buscar(idEstadoEliminado);
            usuarios.setIdEstadoUsuario(estadoEliminado);
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(String nomUser) {
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByNomUsuario")
                    .setParameter("nomUsuario", nomUser)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailConsti = listaResultado.get(i).getMailInstitucional();
                    String mailPers = listaResultado.get(i).getMailPersonal();
                    String telefono = listaResultado.get(i).getTelefono();
                    String documento = listaResultado.get(i).getDocumento();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRol = listaResultado.get(i).getRol();
                    EstadoUsuario idEstado=listaResultado.get(i).getIdEstadoUsuario();
                    
                    user = Usuarios.builder()
                    .idUsuario(idUser)
                    .nomUsuario(nomUsuario)
                    .nombre1(nombre1)
                    .nombre2(nombre2)
                    .apellido1(apellido1)
                    .apellido2(apellido2)
                    .idEstadoUsuario(idEstado)
                    .documento(documento)
                    .telefono(telefono)
                    .mailInstitucional(mailConsti)
                    .mailPersonal(mailPers)
                    .genero(genero)
                    .fecNac(fecNac)
                    .Itr(idItr)
                    .departamento(idDepto)
                    .Localidad(idLocal)
                    .contrasenia(contrasenia)
                    .Rol(idRol)
                    .build();
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
    
    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Usuarios usuarioLogin(String nomUser, String password){
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByNomUsuarioAndPassword")
                    .setParameter("nomUsuario", nomUser)
                    .setParameter("contrasenia", password)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailConsti = listaResultado.get(i).getMailInstitucional();
                    String mailPers = listaResultado.get(i).getMailPersonal();
                    String telefono = listaResultado.get(i).getTelefono();
                    String documento = listaResultado.get(i).getDocumento();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRol = listaResultado.get(i).getRol();
                    EstadoUsuario idEstado=listaResultado.get(i).getIdEstadoUsuario();
                    
                    user = Usuarios.builder()
                    .idUsuario(idUser)
                    .nomUsuario(nomUsuario)
                    .nombre1(nombre1)
                    .nombre2(nombre2)
                    .apellido1(apellido1)
                    .apellido2(apellido2)
                    .idEstadoUsuario(idEstado)
                    .documento(documento)
                    .telefono(telefono)
                    .mailInstitucional(mailConsti)
                    .mailPersonal(mailPers)
                    .genero(genero)
                    .fecNac(fecNac)
                    .Itr(idItr)
                    .departamento(idDepto)
                    .Localidad(idLocal)
                    .contrasenia(contrasenia)
                    .Rol(idRol)
                    .build();
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
     public Usuarios findByDocumento(String documento){
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByDocumento")
                    .setParameter("documento", documento)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailConsti = listaResultado.get(i).getMailInstitucional();
                    String mailPers = listaResultado.get(i).getMailPersonal();
                    String telefono = listaResultado.get(i).getTelefono();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRolStr = listaResultado.get(i).getRol();
                    
                    user = new Usuarios(idUser, nomUsuario, apellido1, nombre1, fecNac, contrasenia, genero,
                              mailConsti, telefono, documento,mailPers);

                    user.setDepartamento(idDepto);
                    user.setItr(idItr);
                    user.setLocalidad(idLocal);
                    user.setRol(idRolStr);
                    user.setApellido2(apellido2);
                    user.setNombre2(nombre2);
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
    
     public Usuarios findByMailC(String mail){
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByMailInstitucional")
                    .setParameter("mailInstitucional", mail)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String documento = listaResultado.get(i).getDocumento();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailPers = listaResultado.get(i).getMailPersonal();
                    String telefono = listaResultado.get(i).getTelefono();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRolStr = listaResultado.get(i).getRol();
                    
                    user = new Usuarios(idUser, nomUsuario, apellido1, nombre1, fecNac, contrasenia, genero,
                              mail, telefono, documento,mailPers);

                    user.setDepartamento(idDepto);
                    user.setItr(idItr);
                    user.setLocalidad(idLocal);
                    user.setRol(idRolStr);
                    user.setApellido2(apellido2);
                    user.setNombre2(nombre2);
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
     public Usuarios findByMailP(String mail){
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByMailPersonal")
                    .setParameter("mailPersonal", mail)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String documento = listaResultado.get(i).getDocumento();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailConsti = listaResultado.get(i).getMailInstitucional();
                    String telefono = listaResultado.get(i).getTelefono();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRolStr = listaResultado.get(i).getRol();
                    
                    user = new Usuarios(idUser, nomUsuario, apellido1, nombre1, fecNac, contrasenia, genero,
                              mailConsti, telefono, documento,mail);

                    user.setDepartamento(idDepto);
                    user.setItr(idItr);
                    user.setLocalidad(idLocal);
                    user.setRol(idRolStr);
                    user.setApellido2(apellido2);
                    user.setNombre2(nombre2);
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
     public Usuarios findByTelefono(String tel){
        EntityManager em = getEntityManager();
        Usuarios user = null;
        try{
            List<Usuarios> listaResultado = em.createNamedQuery("Usuarios.findByTelefono")
                    .setParameter("telefono", tel)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idUser = listaResultado.get(i).getIdUsuario();
                    String documento = listaResultado.get(i).getDocumento();
                    String nomUsuario = listaResultado.get(i).getNomUsuario();
                    String nombre1 = listaResultado.get(i).getNombre1();
                    String nombre2 = listaResultado.get(i).getNombre2();
                    String apellido1 = listaResultado.get(i).getApellido1();
                    String apellido2 = listaResultado.get(i).getApellido2();
                    Date fecNac = listaResultado.get(i).getFecNac();
                    String contrasenia = listaResultado.get(i).getContrasenia();
                    char genero = listaResultado.get(i).getGenero();
                    Localidad idLocal = listaResultado.get(i).getLocalidad();
                    String mailPers = listaResultado.get(i).getMailPersonal();
                    String mailConsti = listaResultado.get(i).getMailInstitucional();
                    Departamento idDepto = listaResultado.get(i).getDepartamento();
                    Itr idItr = listaResultado.get(i).getItr();
                    Roles idRolStr = listaResultado.get(i).getRol();
                    
                    user = new Usuarios(idUser, nomUsuario, apellido1, nombre1, fecNac, contrasenia, genero,
                              mailConsti, tel, documento,mailPers);

                    user.setDepartamento(idDepto);
                    user.setItr(idItr);
                    user.setLocalidad(idLocal);
                    user.setRol(idRolStr);
                    user.setApellido2(apellido2);
                    user.setNombre2(nombre2);
                }
            }
                                
             return user;
        }finally {
            em.close();
        }
       
    }
}
