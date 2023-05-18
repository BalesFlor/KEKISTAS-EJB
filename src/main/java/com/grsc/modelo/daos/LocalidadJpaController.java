package com.grsc.modelo.daos;

import com.grsc.logica.ejb.DepartamentoBean;
import com.grsc.logica.ejb.LocalidadBean;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.Localidad;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class LocalidadJpaController implements Serializable {

    public LocalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidad localidad) throws PreexistingEntityException, Exception {
        if (localidad.getUsuariosList() == null) {
            localidad.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDepartamento = localidad.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getIdDepartamento());
                localidad.setIdDepartamento(idDepartamento);
            }
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : localidad.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdUsuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            localidad.setUsuariosList(attachedUsuariosList);
            em.persist(localidad);
            if (idDepartamento != null) {
                idDepartamento.getLocalidadList().add(localidad);
                idDepartamento = em.merge(idDepartamento);
            }
            for (Usuarios usuariosListUsuarios : localidad.getUsuariosList()) {
                Localidad oldIdLocalidadOfUsuariosListUsuarios = usuariosListUsuarios.getLocalidad();
                usuariosListUsuarios.setLocalidad(localidad);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdLocalidadOfUsuariosListUsuarios != null) {
                    oldIdLocalidadOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdLocalidadOfUsuariosListUsuarios = em.merge(oldIdLocalidadOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocalidad(localidad.getIdLocalidad()) != null) {
                throw new PreexistingEntityException("Localidad " + localidad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidad localidad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad persistentLocalidad = em.find(Localidad.class, localidad.getIdLocalidad());
            Departamento idDepartamentoOld = persistentLocalidad.getIdDepartamento();
            Departamento idDepartamentoNew = localidad.getIdDepartamento();
            List<Usuarios> usuariosListOld = persistentLocalidad.getUsuariosList();
            List<Usuarios> usuariosListNew = localidad.getUsuariosList();
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                localidad.setIdDepartamento(idDepartamentoNew);
            }
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            localidad.setUsuariosList(usuariosListNew);
            localidad = em.merge(localidad);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getLocalidadList().remove(localidad);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getLocalidadList().add(localidad);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    usuariosListOldUsuarios.setLocalidad(null);
                    usuariosListOldUsuarios = em.merge(usuariosListOldUsuarios);
                }
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Localidad oldIdLocalidadOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getLocalidad();
                    usuariosListNewUsuarios.setLocalidad(localidad);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdLocalidadOfUsuariosListNewUsuarios != null && !oldIdLocalidadOfUsuariosListNewUsuarios.equals(localidad)) {
                        oldIdLocalidadOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdLocalidadOfUsuariosListNewUsuarios = em.merge(oldIdLocalidadOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = localidad.getIdLocalidad();
                if (findLocalidad(id) == null) {
                    throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad localidad;
            try {
                localidad = em.getReference(Localidad.class, id);
                localidad.getIdLocalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.", enfe);
            }
            Departamento idDepartamento = localidad.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getLocalidadList().remove(localidad);
                idDepartamento = em.merge(idDepartamento);
            }
            List<Usuarios> usuariosList = localidad.getUsuariosList();
            for (Usuarios usuariosListUsuarios : usuariosList) {
                usuariosListUsuarios.setLocalidad(null);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
            }
            em.remove(localidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localidad> findLocalidadEntities() {
        return findLocalidadEntities(true, -1, -1);
    }

    public List<Localidad> findLocalidadEntities(int maxResults, int firstResult) {
        return findLocalidadEntities(false, maxResults, firstResult);
    }

    private List<Localidad> findLocalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidad.class));
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

    public Localidad findLocalidad(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidad> rt = cq.from(Localidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Localidad> findByDepartamento(BigInteger idDepartamento){
        EntityManager em = getEntityManager();
        DepartamentoBean deptoBean= new DepartamentoBean();
        Departamento depto= deptoBean.buscarDepto(idDepartamento);
        List<Localidad> listaResultado;
        try{
            listaResultado = em.createNamedQuery("Localidad.findByIdDepartamento")
                    .setParameter("idDepartamento", depto)
                    .getResultList();
            for(int i=0; i<listaResultado.size();i++){
                System.out.println(listaResultado.get(i).getNomLocalidad());
            }
            
            }finally {
            em.close();
        }
       return listaResultado;
    }
    
    public List<String> findNombreByDepartamento(BigInteger idDepartamento){
        EntityManager em = getEntityManager();
        LocalidadBean localsBean= new LocalidadBean();
        
        List<Localidad> listaResultado= findByDepartamento(idDepartamento);
        List<String> listaNomLocals = new ArrayList<>(); 
        
        try {
            listaNomLocals.add("Seleccione una localidad*");

            //Con este for cargaremos los nombres de cada localidad desde la listaLocals a la listaNomLocals
            for (int i = 1; i < listaResultado.size(); i++) {
                listaNomLocals.add(listaResultado.get(i).getNomLocalidad());
            }
        } finally {
            em.close();
        }
       return listaNomLocals;
    }

}
