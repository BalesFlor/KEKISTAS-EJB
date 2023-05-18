package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.AccionJustificacion;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JustificacionJpaController implements Serializable {

    public JustificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Justificacion justificacion) {
        if (justificacion.getAccionJustificacionList() == null) {
            justificacion.setAccionJustificacionList(new ArrayList<AccionJustificacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante idUsuario = justificacion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                justificacion.setIdUsuario(idUsuario);
            }
            List<AccionJustificacion> attachedAccionJustificacionList = new ArrayList<AccionJustificacion>();
            for (AccionJustificacion accionJustificacionListAccionJustificacionToAttach : justificacion.getAccionJustificacionList()) {
                accionJustificacionListAccionJustificacionToAttach = em.getReference(accionJustificacionListAccionJustificacionToAttach.getClass(), accionJustificacionListAccionJustificacionToAttach.getAccionJustificacionPK());
                attachedAccionJustificacionList.add(accionJustificacionListAccionJustificacionToAttach);
            }
            justificacion.setAccionJustificacionList(attachedAccionJustificacionList);
            em.persist(justificacion);
            if (idUsuario != null) {
                idUsuario.getJustificacionList().add(justificacion);
                idUsuario = em.merge(idUsuario);
            }
            for (AccionJustificacion accionJustificacionListAccionJustificacion : justificacion.getAccionJustificacionList()) {
                Justificacion oldJustificacionOfAccionJustificacionListAccionJustificacion = accionJustificacionListAccionJustificacion.getJustificacion();
                accionJustificacionListAccionJustificacion.setJustificacion(justificacion);
                accionJustificacionListAccionJustificacion = em.merge(accionJustificacionListAccionJustificacion);
                if (oldJustificacionOfAccionJustificacionListAccionJustificacion != null) {
                    oldJustificacionOfAccionJustificacionListAccionJustificacion.getAccionJustificacionList().remove(accionJustificacionListAccionJustificacion);
                    oldJustificacionOfAccionJustificacionListAccionJustificacion = em.merge(oldJustificacionOfAccionJustificacionListAccionJustificacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Justificacion justificacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Justificacion persistentJustificacion = em.find(Justificacion.class, justificacion.getIdJustificacion());
            Estudiante idUsuarioOld = persistentJustificacion.getIdUsuario();
            Estudiante idUsuarioNew = justificacion.getIdUsuario();
            List<AccionJustificacion> accionJustificacionListOld = persistentJustificacion.getAccionJustificacionList();
            List<AccionJustificacion> accionJustificacionListNew = justificacion.getAccionJustificacionList();
            List<String> illegalOrphanMessages = null;
            for (AccionJustificacion accionJustificacionListOldAccionJustificacion : accionJustificacionListOld) {
                if (!accionJustificacionListNew.contains(accionJustificacionListOldAccionJustificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionJustificacion " + accionJustificacionListOldAccionJustificacion + " since its justificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                justificacion.setIdUsuario(idUsuarioNew);
            }
            List<AccionJustificacion> attachedAccionJustificacionListNew = new ArrayList<AccionJustificacion>();
            for (AccionJustificacion accionJustificacionListNewAccionJustificacionToAttach : accionJustificacionListNew) {
                accionJustificacionListNewAccionJustificacionToAttach = em.getReference(accionJustificacionListNewAccionJustificacionToAttach.getClass(), accionJustificacionListNewAccionJustificacionToAttach.getAccionJustificacionPK());
                attachedAccionJustificacionListNew.add(accionJustificacionListNewAccionJustificacionToAttach);
            }
            accionJustificacionListNew = attachedAccionJustificacionListNew;
            justificacion.setAccionJustificacionList(accionJustificacionListNew);
            justificacion = em.merge(justificacion);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getJustificacionList().remove(justificacion);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getJustificacionList().add(justificacion);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (AccionJustificacion accionJustificacionListNewAccionJustificacion : accionJustificacionListNew) {
                if (!accionJustificacionListOld.contains(accionJustificacionListNewAccionJustificacion)) {
                    Justificacion oldJustificacionOfAccionJustificacionListNewAccionJustificacion = accionJustificacionListNewAccionJustificacion.getJustificacion();
                    accionJustificacionListNewAccionJustificacion.setJustificacion(justificacion);
                    accionJustificacionListNewAccionJustificacion = em.merge(accionJustificacionListNewAccionJustificacion);
                    if (oldJustificacionOfAccionJustificacionListNewAccionJustificacion != null && !oldJustificacionOfAccionJustificacionListNewAccionJustificacion.equals(justificacion)) {
                        oldJustificacionOfAccionJustificacionListNewAccionJustificacion.getAccionJustificacionList().remove(accionJustificacionListNewAccionJustificacion);
                        oldJustificacionOfAccionJustificacionListNewAccionJustificacion = em.merge(oldJustificacionOfAccionJustificacionListNewAccionJustificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = justificacion.getIdJustificacion();
                if (findJustificacion(id) == null) {
                    throw new NonexistentEntityException("The justificacion with id " + id + " no longer exists.");
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
            Justificacion justificacion;
            try {
                justificacion = em.getReference(Justificacion.class, id);
                justificacion.getIdJustificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The justificacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccionJustificacion> accionJustificacionListOrphanCheck = justificacion.getAccionJustificacionList();
            for (AccionJustificacion accionJustificacionListOrphanCheckAccionJustificacion : accionJustificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Justificacion (" + justificacion + ") cannot be destroyed since the AccionJustificacion " + accionJustificacionListOrphanCheckAccionJustificacion + " in its accionJustificacionList field has a non-nullable justificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudiante idUsuario = justificacion.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getJustificacionList().remove(justificacion);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(justificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Justificacion> findJustificacionEntities() {
        return findJustificacionEntities(true, -1, -1);
    }

    public List<Justificacion> findJustificacionEntities(int maxResults, int firstResult) {
        return findJustificacionEntities(false, maxResults, firstResult);
    }

    private List<Justificacion> findJustificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Justificacion.class));
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

    public Justificacion findJustificacion(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Justificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getJustificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Justificacion> rt = cq.from(Justificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
