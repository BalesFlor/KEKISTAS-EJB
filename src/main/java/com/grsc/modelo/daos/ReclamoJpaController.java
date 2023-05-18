package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.AccionReclamo;
import com.grsc.modelo.entities.Reclamo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ReclamoJpaController implements Serializable {

    public ReclamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reclamo reclamo) {
        if (reclamo.getAccionReclamoList() == null) {
            reclamo.setAccionReclamoList(new ArrayList<AccionReclamo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante idUsuario = reclamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                reclamo.setIdUsuario(idUsuario);
            }
            List<AccionReclamo> attachedAccionReclamoList = new ArrayList<AccionReclamo>();
            for (AccionReclamo accionReclamoListAccionReclamoToAttach : reclamo.getAccionReclamoList()) {
                accionReclamoListAccionReclamoToAttach = em.getReference(accionReclamoListAccionReclamoToAttach.getClass(), accionReclamoListAccionReclamoToAttach.getAccionReclamoPK());
                attachedAccionReclamoList.add(accionReclamoListAccionReclamoToAttach);
            }
            reclamo.setAccionReclamoList(attachedAccionReclamoList);
            em.persist(reclamo);
            if (idUsuario != null) {
                idUsuario.getReclamoList().add(reclamo);
                idUsuario = em.merge(idUsuario);
            }
            for (AccionReclamo accionReclamoListAccionReclamo : reclamo.getAccionReclamoList()) {
                Reclamo oldReclamoOfAccionReclamoListAccionReclamo = accionReclamoListAccionReclamo.getReclamo();
                accionReclamoListAccionReclamo.setReclamo(reclamo);
                accionReclamoListAccionReclamo = em.merge(accionReclamoListAccionReclamo);
                if (oldReclamoOfAccionReclamoListAccionReclamo != null) {
                    oldReclamoOfAccionReclamoListAccionReclamo.getAccionReclamoList().remove(accionReclamoListAccionReclamo);
                    oldReclamoOfAccionReclamoListAccionReclamo = em.merge(oldReclamoOfAccionReclamoListAccionReclamo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reclamo reclamo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reclamo persistentReclamo = em.find(Reclamo.class, reclamo.getIdReclamo());
            Estudiante idUsuarioOld = persistentReclamo.getIdUsuario();
            Estudiante idUsuarioNew = reclamo.getIdUsuario();
            List<AccionReclamo> accionReclamoListOld = persistentReclamo.getAccionReclamoList();
            List<AccionReclamo> accionReclamoListNew = reclamo.getAccionReclamoList();
            List<String> illegalOrphanMessages = null;
            for (AccionReclamo accionReclamoListOldAccionReclamo : accionReclamoListOld) {
                if (!accionReclamoListNew.contains(accionReclamoListOldAccionReclamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionReclamo " + accionReclamoListOldAccionReclamo + " since its reclamo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                reclamo.setIdUsuario(idUsuarioNew);
            }
            List<AccionReclamo> attachedAccionReclamoListNew = new ArrayList<AccionReclamo>();
            for (AccionReclamo accionReclamoListNewAccionReclamoToAttach : accionReclamoListNew) {
                accionReclamoListNewAccionReclamoToAttach = em.getReference(accionReclamoListNewAccionReclamoToAttach.getClass(), accionReclamoListNewAccionReclamoToAttach.getAccionReclamoPK());
                attachedAccionReclamoListNew.add(accionReclamoListNewAccionReclamoToAttach);
            }
            accionReclamoListNew = attachedAccionReclamoListNew;
            reclamo.setAccionReclamoList(accionReclamoListNew);
            reclamo = em.merge(reclamo);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getReclamoList().remove(reclamo);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getReclamoList().add(reclamo);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (AccionReclamo accionReclamoListNewAccionReclamo : accionReclamoListNew) {
                if (!accionReclamoListOld.contains(accionReclamoListNewAccionReclamo)) {
                    Reclamo oldReclamoOfAccionReclamoListNewAccionReclamo = accionReclamoListNewAccionReclamo.getReclamo();
                    accionReclamoListNewAccionReclamo.setReclamo(reclamo);
                    accionReclamoListNewAccionReclamo = em.merge(accionReclamoListNewAccionReclamo);
                    if (oldReclamoOfAccionReclamoListNewAccionReclamo != null && !oldReclamoOfAccionReclamoListNewAccionReclamo.equals(reclamo)) {
                        oldReclamoOfAccionReclamoListNewAccionReclamo.getAccionReclamoList().remove(accionReclamoListNewAccionReclamo);
                        oldReclamoOfAccionReclamoListNewAccionReclamo = em.merge(oldReclamoOfAccionReclamoListNewAccionReclamo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = reclamo.getIdReclamo();
                if (findReclamo(id) == null) {
                    throw new NonexistentEntityException("The reclamo with id " + id + " no longer exists.");
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
            Reclamo reclamo;
            try {
                reclamo = em.getReference(Reclamo.class, id);
                reclamo.getIdReclamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reclamo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccionReclamo> accionReclamoListOrphanCheck = reclamo.getAccionReclamoList();
            for (AccionReclamo accionReclamoListOrphanCheckAccionReclamo : accionReclamoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reclamo (" + reclamo + ") cannot be destroyed since the AccionReclamo " + accionReclamoListOrphanCheckAccionReclamo + " in its accionReclamoList field has a non-nullable reclamo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudiante idUsuario = reclamo.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getReclamoList().remove(reclamo);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(reclamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reclamo> findReclamoEntities() {
        return findReclamoEntities(true, -1, -1);
    }

    public List<Reclamo> findReclamoEntities(int maxResults, int firstResult) {
        return findReclamoEntities(false, maxResults, firstResult);
    }

    private List<Reclamo> findReclamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reclamo.class));
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

    public Reclamo findReclamo(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reclamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getReclamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reclamo> rt = cq.from(Reclamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
