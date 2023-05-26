package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.logica.ejb.AnalistaBean;
import com.grsc.logica.ejb.DocenteBean;
import com.grsc.logica.ejb.EstadoUsuarioBean;
import com.grsc.logica.ejb.EstudianteBean;
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
import com.grsc.modelo.entities.Reclamo;
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
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Tutor tutorOld = persistentUsuarios.getTutor();
            Tutor tutorNew = usuarios.getTutor();
            Analista analistaOld = persistentUsuarios.getAnalista();
            Analista analistaNew = usuarios.getAnalista();
            Departamento idDepartamentoOld = persistentUsuarios.getDepartamento();
            Departamento idDepartamentoNew = usuarios.getDepartamento();
            Itr idItrOld = persistentUsuarios.getItr();
            Itr idItrNew = usuarios.getItr();
            Localidad idLocalidadOld = persistentUsuarios.getLocalidad();
            Localidad idLocalidadNew = usuarios.getLocalidad();
            Roles idRolOld = persistentUsuarios.getRol();
            Roles idRolNew = usuarios.getRol();
            Estudiante estudianteOld = persistentUsuarios.getEstudiante();
            Estudiante estudianteNew = usuarios.getEstudiante();
            List<String> illegalOrphanMessages = null;
            if (tutorOld != null && !tutorOld.equals(tutorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Tutor " + tutorOld + " since its usuarios field is not nullable.");
            }
            if (analistaOld != null && !analistaOld.equals(analistaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Analista " + analistaOld + " since its usuarios field is not nullable.");
            }
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Estudiante " + estudianteOld + " since its usuarios field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tutorNew != null) {
                tutorNew = em.getReference(tutorNew.getClass(), tutorNew.getIdUsuario());
                usuarios.setTutor(tutorNew);
            }
            if (analistaNew != null) {
                analistaNew = em.getReference(analistaNew.getClass(), analistaNew.getIdUsuario());
                usuarios.setAnalista(analistaNew);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                usuarios.setDepartamento(idDepartamentoNew);
            }
            if (idItrNew != null) {
                idItrNew = em.getReference(idItrNew.getClass(), idItrNew.getIdItr());
                usuarios.setItr(idItrNew);
            }
            if (idLocalidadNew != null) {
                idLocalidadNew = em.getReference(idLocalidadNew.getClass(), idLocalidadNew.getIdLocalidad());
                usuarios.setLocalidad(idLocalidadNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuarios.setRol(idRolNew);
            }
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getIdUsuario());
                usuarios.setEstudiante(estudianteNew);
            }
            usuarios = em.merge(usuarios);
            if (tutorNew != null && !tutorNew.equals(tutorOld)) {
                Usuarios oldUsuariosOfTutor = tutorNew.getUsuarios();
                if (oldUsuariosOfTutor != null) {
                    oldUsuariosOfTutor.setTutor(null);
                    oldUsuariosOfTutor = em.merge(oldUsuariosOfTutor);
                }
                tutorNew.setUsuarios(usuarios);
                tutorNew = em.merge(tutorNew);
            }
            if (analistaNew != null && !analistaNew.equals(analistaOld)) {
                Usuarios oldUsuariosOfAnalista = analistaNew.getUsuarios();
                if (oldUsuariosOfAnalista != null) {
                    oldUsuariosOfAnalista.setAnalista(null);
                    oldUsuariosOfAnalista = em.merge(oldUsuariosOfAnalista);
                }
                analistaNew.setUsuarios(usuarios);
                analistaNew = em.merge(analistaNew);
            }
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getUsuariosList().remove(usuarios);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getUsuariosList().add(usuarios);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            if (idItrOld != null && !idItrOld.equals(idItrNew)) {
                idItrOld.getUsuariosList().remove(usuarios);
                idItrOld = em.merge(idItrOld);
            }
            if (idItrNew != null && !idItrNew.equals(idItrOld)) {
                idItrNew.getUsuariosList().add(usuarios);
                idItrNew = em.merge(idItrNew);
            }
            if (idLocalidadOld != null && !idLocalidadOld.equals(idLocalidadNew)) {
                idLocalidadOld.getUsuariosList().remove(usuarios);
                idLocalidadOld = em.merge(idLocalidadOld);
            }
            if (idLocalidadNew != null && !idLocalidadNew.equals(idLocalidadOld)) {
                idLocalidadNew.getUsuariosList().add(usuarios);
                idLocalidadNew = em.merge(idLocalidadNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuariosList().remove(usuarios);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuariosList().add(usuarios);
                idRolNew = em.merge(idRolNew);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                Usuarios oldUsuariosOfEstudiante = estudianteNew.getUsuarios();
                if (oldUsuariosOfEstudiante != null) {
                    oldUsuariosOfEstudiante.setEstudiante(null);
                    oldUsuariosOfEstudiante = em.merge(oldUsuariosOfEstudiante);
                }
                estudianteNew.setUsuarios(usuarios);
                estudianteNew = em.merge(estudianteNew);
            }
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
