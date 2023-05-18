package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.AccionConstancia;
import com.grsc.modelo.entities.AccionConstanciaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Constancia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AccionConstanciaJpaController implements Serializable {

    public AccionConstanciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccionConstancia accionConstancia) throws PreexistingEntityException, Exception {
        if (accionConstancia.getAccionConstanciaPK() == null) {
            accionConstancia.setAccionConstanciaPK(new AccionConstanciaPK());
        }
        accionConstancia.getAccionConstanciaPK().setIdConstancia(accionConstancia.getConstancia().getIdConstancia());
        accionConstancia.getAccionConstanciaPK().setIdUsuario(accionConstancia.getAnalista().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Analista analista = accionConstancia.getAnalista();
            if (analista != null) {
                analista = em.getReference(analista.getClass(), analista.getIdUsuario());
                accionConstancia.setAnalista(analista);
            }
            Constancia constancia = accionConstancia.getConstancia();
            if (constancia != null) {
                constancia = em.getReference(constancia.getClass(), constancia.getIdConstancia());
                accionConstancia.setConstancia(constancia);
            }
            em.persist(accionConstancia);
            if (analista != null) {
                analista.getAccionConstanciaList().add(accionConstancia);
                analista = em.merge(analista);
            }
            if (constancia != null) {
                constancia.getAccionConstanciaList().add(accionConstancia);
                constancia = em.merge(constancia);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccionConstancia(accionConstancia.getAccionConstanciaPK()) != null) {
                throw new PreexistingEntityException("AccionConstancia " + accionConstancia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccionConstancia accionConstancia) throws NonexistentEntityException, Exception {
        accionConstancia.getAccionConstanciaPK().setIdConstancia(accionConstancia.getConstancia().getIdConstancia());
        accionConstancia.getAccionConstanciaPK().setIdUsuario(accionConstancia.getAnalista().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionConstancia persistentAccionConstancia = em.find(AccionConstancia.class, accionConstancia.getAccionConstanciaPK());
            Analista analistaOld = persistentAccionConstancia.getAnalista();
            Analista analistaNew = accionConstancia.getAnalista();
            Constancia constanciaOld = persistentAccionConstancia.getConstancia();
            Constancia constanciaNew = accionConstancia.getConstancia();
            if (analistaNew != null) {
                analistaNew = em.getReference(analistaNew.getClass(), analistaNew.getIdUsuario());
                accionConstancia.setAnalista(analistaNew);
            }
            if (constanciaNew != null) {
                constanciaNew = em.getReference(constanciaNew.getClass(), constanciaNew.getIdConstancia());
                accionConstancia.setConstancia(constanciaNew);
            }
            accionConstancia = em.merge(accionConstancia);
            if (analistaOld != null && !analistaOld.equals(analistaNew)) {
                analistaOld.getAccionConstanciaList().remove(accionConstancia);
                analistaOld = em.merge(analistaOld);
            }
            if (analistaNew != null && !analistaNew.equals(analistaOld)) {
                analistaNew.getAccionConstanciaList().add(accionConstancia);
                analistaNew = em.merge(analistaNew);
            }
            if (constanciaOld != null && !constanciaOld.equals(constanciaNew)) {
                constanciaOld.getAccionConstanciaList().remove(accionConstancia);
                constanciaOld = em.merge(constanciaOld);
            }
            if (constanciaNew != null && !constanciaNew.equals(constanciaOld)) {
                constanciaNew.getAccionConstanciaList().add(accionConstancia);
                constanciaNew = em.merge(constanciaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AccionConstanciaPK id = accionConstancia.getAccionConstanciaPK();
                if (findAccionConstancia(id) == null) {
                    throw new NonexistentEntityException("The accionConstancia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AccionConstanciaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionConstancia accionConstancia;
            try {
                accionConstancia = em.getReference(AccionConstancia.class, id);
                accionConstancia.getAccionConstanciaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accionConstancia with id " + id + " no longer exists.", enfe);
            }
            Analista analista = accionConstancia.getAnalista();
            if (analista != null) {
                analista.getAccionConstanciaList().remove(accionConstancia);
                analista = em.merge(analista);
            }
            Constancia constancia = accionConstancia.getConstancia();
            if (constancia != null) {
                constancia.getAccionConstanciaList().remove(accionConstancia);
                constancia = em.merge(constancia);
            }
            em.remove(accionConstancia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccionConstancia> findAccionConstanciaEntities() {
        return findAccionConstanciaEntities(true, -1, -1);
    }

    public List<AccionConstancia> findAccionConstanciaEntities(int maxResults, int firstResult) {
        return findAccionConstanciaEntities(false, maxResults, firstResult);
    }

    private List<AccionConstancia> findAccionConstanciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccionConstancia.class));
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

    public AccionConstancia findAccionConstancia(AccionConstanciaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccionConstancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccionConstanciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccionConstancia> rt = cq.from(AccionConstancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
