package com.grsc.modelo.daos;

import com.correo.exceptions.IllegalOrphanException;
import com.correo.exceptions.NonexistentEntityException;
import com.correo.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.EstadoItr;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Itr;
import java.math.BigInteger;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EstadoItrJpaController implements Serializable {

    public EstadoItrJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoItr estadoItr) throws PreexistingEntityException, Exception {
        if (estadoItr.getItrCollection() == null) {
            estadoItr.setItrCollection(new ArrayList<Itr>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Itr> attachedItrCollection = new ArrayList<Itr>();
            for (Itr itrCollectionItrToAttach : estadoItr.getItrCollection()) {
                itrCollectionItrToAttach = em.getReference(itrCollectionItrToAttach.getClass(), itrCollectionItrToAttach.getIdItr());
                attachedItrCollection.add(itrCollectionItrToAttach);
            }
            estadoItr.setItrCollection(attachedItrCollection);
            em.persist(estadoItr);
            for (Itr itrCollectionItr : estadoItr.getItrCollection()) {
                EstadoItr oldIdEstadoOfItrCollectionItr = itrCollectionItr.getIdEstado();
                itrCollectionItr.setIdEstado(estadoItr);
                itrCollectionItr = em.merge(itrCollectionItr);
                if (oldIdEstadoOfItrCollectionItr != null) {
                    oldIdEstadoOfItrCollectionItr.getItrCollection().remove(itrCollectionItr);
                    oldIdEstadoOfItrCollectionItr = em.merge(oldIdEstadoOfItrCollectionItr);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoItr(estadoItr.getIdEstado()) != null) {
                throw new PreexistingEntityException("EstadoItr " + estadoItr + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoItr estadoItr) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoItr persistentEstadoItr = em.find(EstadoItr.class, estadoItr.getIdEstado());
            Collection<Itr> itrCollectionOld = persistentEstadoItr.getItrCollection();
            Collection<Itr> itrCollectionNew = estadoItr.getItrCollection();
            List<String> illegalOrphanMessages = null;
            for (Itr itrCollectionOldItr : itrCollectionOld) {
                if (!itrCollectionNew.contains(itrCollectionOldItr)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Itr " + itrCollectionOldItr + " since its idEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Itr> attachedItrCollectionNew = new ArrayList<Itr>();
            for (Itr itrCollectionNewItrToAttach : itrCollectionNew) {
                itrCollectionNewItrToAttach = em.getReference(itrCollectionNewItrToAttach.getClass(), itrCollectionNewItrToAttach.getIdItr());
                attachedItrCollectionNew.add(itrCollectionNewItrToAttach);
            }
            itrCollectionNew = attachedItrCollectionNew;
            estadoItr.setItrCollection(itrCollectionNew);
            estadoItr = em.merge(estadoItr);
            for (Itr itrCollectionNewItr : itrCollectionNew) {
                if (!itrCollectionOld.contains(itrCollectionNewItr)) {
                    EstadoItr oldIdEstadoOfItrCollectionNewItr = itrCollectionNewItr.getIdEstado();
                    itrCollectionNewItr.setIdEstado(estadoItr);
                    itrCollectionNewItr = em.merge(itrCollectionNewItr);
                    if (oldIdEstadoOfItrCollectionNewItr != null && !oldIdEstadoOfItrCollectionNewItr.equals(estadoItr)) {
                        oldIdEstadoOfItrCollectionNewItr.getItrCollection().remove(itrCollectionNewItr);
                        oldIdEstadoOfItrCollectionNewItr = em.merge(oldIdEstadoOfItrCollectionNewItr);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = estadoItr.getIdEstado();
                if (findEstadoItr(id) == null) {
                    throw new NonexistentEntityException("The estadoItr with id " + id + " no longer exists.");
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
            EstadoItr estadoItr;
            try {
                estadoItr = em.getReference(EstadoItr.class, id);
                estadoItr.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoItr with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Itr> itrCollectionOrphanCheck = estadoItr.getItrCollection();
            for (Itr itrCollectionOrphanCheckItr : itrCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoItr (" + estadoItr + ") cannot be destroyed since the Itr " + itrCollectionOrphanCheckItr + " in its itrCollection field has a non-nullable idEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoItr);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoItr> findEstadoItrEntities() {
        return findEstadoItrEntities(true, -1, -1);
    }

    public List<EstadoItr> findEstadoItrEntities(int maxResults, int firstResult) {
        return findEstadoItrEntities(false, maxResults, firstResult);
    }

    private List<EstadoItr> findEstadoItrEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoItr.class));
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

    public EstadoItr findEstadoItr(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoItr.class, id);
        } finally {
            em.close();
        }
    }

      public EstadoItr findEstadoItr(String nombre) {
        EntityManager em = getEntityManager();
        EstadoItr estadoRes = new EstadoItr();
        try {
            List<EstadoItr> listaResultado = em.createNamedQuery("EstadoItr.findByNomEstado")
                    .setParameter("nomEstado", nombre)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger id = listaResultado.get(i).getIdEstado();
                    String nom = listaResultado.get(i).getNomEstado();

                    estadoRes = EstadoItr.builder()
                            .idEstado(id)
                            .nomEstado(nom)
                            .build();
                }
            }
            return estadoRes;
        } finally {
            em.close();
        }
    }

    
    
    public int getEstadoItrCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoItr> rt = cq.from(EstadoItr.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
