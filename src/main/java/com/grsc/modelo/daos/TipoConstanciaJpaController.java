package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Constancia;
import com.grsc.modelo.entities.TipoConstancia;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TipoConstanciaJpaController implements Serializable {

    public TipoConstanciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoConstancia tipoConstancia) throws PreexistingEntityException, Exception {
        if (tipoConstancia.getConstanciaList() == null) {
            tipoConstancia.setConstanciaList(new ArrayList<Constancia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Constancia> attachedConstanciaList = new ArrayList<Constancia>();
            for (Constancia constanciaListConstanciaToAttach : tipoConstancia.getConstanciaList()) {
                constanciaListConstanciaToAttach = em.getReference(constanciaListConstanciaToAttach.getClass(), constanciaListConstanciaToAttach.getIdConstancia());
                attachedConstanciaList.add(constanciaListConstanciaToAttach);
            }
            tipoConstancia.setConstanciaList(attachedConstanciaList);
            em.persist(tipoConstancia);
            for (Constancia constanciaListConstancia : tipoConstancia.getConstanciaList()) {
                TipoConstancia oldIdTipoConstanciaOfConstanciaListConstancia = constanciaListConstancia.getIdTipoConstancia();
                constanciaListConstancia.setIdTipoConstancia(tipoConstancia);
                constanciaListConstancia = em.merge(constanciaListConstancia);
                if (oldIdTipoConstanciaOfConstanciaListConstancia != null) {
                    oldIdTipoConstanciaOfConstanciaListConstancia.getConstanciaList().remove(constanciaListConstancia);
                    oldIdTipoConstanciaOfConstanciaListConstancia = em.merge(oldIdTipoConstanciaOfConstanciaListConstancia);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoConstancia(tipoConstancia.getIdTipoConstancia()) != null) {
                throw new PreexistingEntityException("TipoConstancia " + tipoConstancia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoConstancia tipoConstancia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoConstancia persistentTipoConstancia = em.find(TipoConstancia.class, tipoConstancia.getIdTipoConstancia());
            List<Constancia> constanciaListOld = persistentTipoConstancia.getConstanciaList();
            List<Constancia> constanciaListNew = tipoConstancia.getConstanciaList();
            List<String> illegalOrphanMessages = null;
            for (Constancia constanciaListOldConstancia : constanciaListOld) {
                if (!constanciaListNew.contains(constanciaListOldConstancia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Constancia " + constanciaListOldConstancia + " since its idTipoConstancia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Constancia> attachedConstanciaListNew = new ArrayList<Constancia>();
            for (Constancia constanciaListNewConstanciaToAttach : constanciaListNew) {
                constanciaListNewConstanciaToAttach = em.getReference(constanciaListNewConstanciaToAttach.getClass(), constanciaListNewConstanciaToAttach.getIdConstancia());
                attachedConstanciaListNew.add(constanciaListNewConstanciaToAttach);
            }
            constanciaListNew = attachedConstanciaListNew;
            tipoConstancia.setConstanciaList(constanciaListNew);
            tipoConstancia = em.merge(tipoConstancia);
            for (Constancia constanciaListNewConstancia : constanciaListNew) {
                if (!constanciaListOld.contains(constanciaListNewConstancia)) {
                    TipoConstancia oldIdTipoConstanciaOfConstanciaListNewConstancia = constanciaListNewConstancia.getIdTipoConstancia();
                    constanciaListNewConstancia.setIdTipoConstancia(tipoConstancia);
                    constanciaListNewConstancia = em.merge(constanciaListNewConstancia);
                    if (oldIdTipoConstanciaOfConstanciaListNewConstancia != null && !oldIdTipoConstanciaOfConstanciaListNewConstancia.equals(tipoConstancia)) {
                        oldIdTipoConstanciaOfConstanciaListNewConstancia.getConstanciaList().remove(constanciaListNewConstancia);
                        oldIdTipoConstanciaOfConstanciaListNewConstancia = em.merge(oldIdTipoConstanciaOfConstanciaListNewConstancia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = tipoConstancia.getIdTipoConstancia();
                if (findTipoConstancia(id) == null) {
                    throw new NonexistentEntityException("The tipoConstancia with id " + id + " no longer exists.");
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
            TipoConstancia tipoConstancia;
            try {
                tipoConstancia = em.getReference(TipoConstancia.class, id);
                tipoConstancia.getIdTipoConstancia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoConstancia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Constancia> constanciaListOrphanCheck = tipoConstancia.getConstanciaList();
            for (Constancia constanciaListOrphanCheckConstancia : constanciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoConstancia (" + tipoConstancia + ") cannot be destroyed since the Constancia " + constanciaListOrphanCheckConstancia + " in its constanciaList field has a non-nullable idTipoConstancia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoConstancia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoConstancia> findTipoConstanciaEntities() {
        return findTipoConstanciaEntities(true, -1, -1);
    }

    public List<TipoConstancia> findTipoConstanciaEntities(int maxResults, int firstResult) {
        return findTipoConstanciaEntities(false, maxResults, firstResult);
    }

    private List<TipoConstancia> findTipoConstanciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoConstancia.class));
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

    public TipoConstancia findTipoConstancia(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoConstancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoConstanciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoConstancia> rt = cq.from(TipoConstancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
