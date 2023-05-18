package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.EstadoPeticion;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class EstadoPeticionJpaController implements Serializable {

    public EstadoPeticionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoPeticion estadoPeticion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(estadoPeticion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoPeticion(estadoPeticion.getIdEstado()) != null) {
                throw new PreexistingEntityException("EstadoPeticion " + estadoPeticion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoPeticion estadoPeticion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            estadoPeticion = em.merge(estadoPeticion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = estadoPeticion.getIdEstado();
                if (findEstadoPeticion(id) == null) {
                    throw new NonexistentEntityException("The estadoPeticion with id " + id + " no longer exists.");
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
            EstadoPeticion estadoPeticion;
            try {
                estadoPeticion = em.getReference(EstadoPeticion.class, id);
                estadoPeticion.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoPeticion with id " + id + " no longer exists.", enfe);
            }
            em.remove(estadoPeticion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoPeticion> findEstadoPeticionEntities() {
        return findEstadoPeticionEntities(true, -1, -1);
    }

    public List<EstadoPeticion> findEstadoPeticionEntities(int maxResults, int firstResult) {
        return findEstadoPeticionEntities(false, maxResults, firstResult);
    }

    private List<EstadoPeticion> findEstadoPeticionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoPeticion.class));
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

    public EstadoPeticion findEstadoPeticion(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoPeticion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoPeticionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoPeticion> rt = cq.from(EstadoPeticion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
