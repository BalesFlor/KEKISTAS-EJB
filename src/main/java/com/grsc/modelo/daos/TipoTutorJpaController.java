package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.TipoTutor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Tutor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TipoTutorJpaController implements Serializable {

    public TipoTutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoTutor tipoTutor) throws PreexistingEntityException, Exception {
        if (tipoTutor.getTutorList() == null) {
            tipoTutor.setTutorList(new ArrayList<Tutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tutor> attachedTutorList = new ArrayList<Tutor>();
            for (Tutor tutorListTutorToAttach : tipoTutor.getTutorList()) {
                tutorListTutorToAttach = em.getReference(tutorListTutorToAttach.getClass(), tutorListTutorToAttach.getIdUsuario());
                attachedTutorList.add(tutorListTutorToAttach);
            }
            tipoTutor.setTutorList(attachedTutorList);
            em.persist(tipoTutor);
            for (Tutor tutorListTutor : tipoTutor.getTutorList()) {
                TipoTutor oldIdTipoTutorOfTutorListTutor = tutorListTutor.getIdTipoTutor();
                tutorListTutor.setIdTipoTutor(tipoTutor);
                tutorListTutor = em.merge(tutorListTutor);
                if (oldIdTipoTutorOfTutorListTutor != null) {
                    oldIdTipoTutorOfTutorListTutor.getTutorList().remove(tutorListTutor);
                    oldIdTipoTutorOfTutorListTutor = em.merge(oldIdTipoTutorOfTutorListTutor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoTutor(tipoTutor.getIdTipoTutor()) != null) {
                throw new PreexistingEntityException("TipoTutor " + tipoTutor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoTutor tipoTutor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoTutor persistentTipoTutor = em.find(TipoTutor.class, tipoTutor.getIdTipoTutor());
            List<Tutor> tutorListOld = persistentTipoTutor.getTutorList();
            List<Tutor> tutorListNew = tipoTutor.getTutorList();
            List<Tutor> attachedTutorListNew = new ArrayList<Tutor>();
            for (Tutor tutorListNewTutorToAttach : tutorListNew) {
                tutorListNewTutorToAttach = em.getReference(tutorListNewTutorToAttach.getClass(), tutorListNewTutorToAttach.getIdUsuario());
                attachedTutorListNew.add(tutorListNewTutorToAttach);
            }
            tutorListNew = attachedTutorListNew;
            tipoTutor.setTutorList(tutorListNew);
            tipoTutor = em.merge(tipoTutor);
            for (Tutor tutorListOldTutor : tutorListOld) {
                if (!tutorListNew.contains(tutorListOldTutor)) {
                    tutorListOldTutor.setIdTipoTutor(null);
                    tutorListOldTutor = em.merge(tutorListOldTutor);
                }
            }
            for (Tutor tutorListNewTutor : tutorListNew) {
                if (!tutorListOld.contains(tutorListNewTutor)) {
                    TipoTutor oldIdTipoTutorOfTutorListNewTutor = tutorListNewTutor.getIdTipoTutor();
                    tutorListNewTutor.setIdTipoTutor(tipoTutor);
                    tutorListNewTutor = em.merge(tutorListNewTutor);
                    if (oldIdTipoTutorOfTutorListNewTutor != null && !oldIdTipoTutorOfTutorListNewTutor.equals(tipoTutor)) {
                        oldIdTipoTutorOfTutorListNewTutor.getTutorList().remove(tutorListNewTutor);
                        oldIdTipoTutorOfTutorListNewTutor = em.merge(oldIdTipoTutorOfTutorListNewTutor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = tipoTutor.getIdTipoTutor();
                if (findTipoTutor(id) == null) {
                    throw new NonexistentEntityException("The tipoTutor with id " + id + " no longer exists.");
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
            TipoTutor tipoTutor;
            try {
                tipoTutor = em.getReference(TipoTutor.class, id);
                tipoTutor.getIdTipoTutor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoTutor with id " + id + " no longer exists.", enfe);
            }
            List<Tutor> tutorList = tipoTutor.getTutorList();
            for (Tutor tutorListTutor : tutorList) {
                tutorListTutor.setIdTipoTutor(null);
                tutorListTutor = em.merge(tutorListTutor);
            }
            em.remove(tipoTutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoTutor> findTipoTutorEntities() {
        return findTipoTutorEntities(true, -1, -1);
    }

    public List<TipoTutor> findTipoTutorEntities(int maxResults, int firstResult) {
        return findTipoTutorEntities(false, maxResults, firstResult);
    }

    private List<TipoTutor> findTipoTutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoTutor.class));
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

    public TipoTutor findTipoTutor(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoTutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoTutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoTutor> rt = cq.from(TipoTutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
