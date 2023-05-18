package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.Area;
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

public class AreaJpaController implements Serializable {

    public AreaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Area area) throws PreexistingEntityException, Exception {
        if (area.getTutorList() == null) {
            area.setTutorList(new ArrayList<Tutor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tutor> attachedTutorList = new ArrayList<Tutor>();
            for (Tutor tutorListTutorToAttach : area.getTutorList()) {
                tutorListTutorToAttach = em.getReference(tutorListTutorToAttach.getClass(), tutorListTutorToAttach.getIdUsuario());
                attachedTutorList.add(tutorListTutorToAttach);
            }
            area.setTutorList(attachedTutorList);
            em.persist(area);
            for (Tutor tutorListTutor : area.getTutorList()) {
                Area oldIdAreaOfTutorListTutor = tutorListTutor.getIdArea();
                tutorListTutor.setIdArea(area);
                tutorListTutor = em.merge(tutorListTutor);
                if (oldIdAreaOfTutorListTutor != null) {
                    oldIdAreaOfTutorListTutor.getTutorList().remove(tutorListTutor);
                    oldIdAreaOfTutorListTutor = em.merge(oldIdAreaOfTutorListTutor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArea(area.getIdArea()) != null) {
                throw new PreexistingEntityException("Area " + area + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Area area) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Area persistentArea = em.find(Area.class, area.getIdArea());
            List<Tutor> tutorListOld = persistentArea.getTutorList();
            List<Tutor> tutorListNew = area.getTutorList();
            List<Tutor> attachedTutorListNew = new ArrayList<Tutor>();
            for (Tutor tutorListNewTutorToAttach : tutorListNew) {
                tutorListNewTutorToAttach = em.getReference(tutorListNewTutorToAttach.getClass(), tutorListNewTutorToAttach.getIdUsuario());
                attachedTutorListNew.add(tutorListNewTutorToAttach);
            }
            tutorListNew = attachedTutorListNew;
            area.setTutorList(tutorListNew);
            area = em.merge(area);
            for (Tutor tutorListOldTutor : tutorListOld) {
                if (!tutorListNew.contains(tutorListOldTutor)) {
                    tutorListOldTutor.setIdArea(null);
                    tutorListOldTutor = em.merge(tutorListOldTutor);
                }
            }
            for (Tutor tutorListNewTutor : tutorListNew) {
                if (!tutorListOld.contains(tutorListNewTutor)) {
                    Area oldIdAreaOfTutorListNewTutor = tutorListNewTutor.getIdArea();
                    tutorListNewTutor.setIdArea(area);
                    tutorListNewTutor = em.merge(tutorListNewTutor);
                    if (oldIdAreaOfTutorListNewTutor != null && !oldIdAreaOfTutorListNewTutor.equals(area)) {
                        oldIdAreaOfTutorListNewTutor.getTutorList().remove(tutorListNewTutor);
                        oldIdAreaOfTutorListNewTutor = em.merge(oldIdAreaOfTutorListNewTutor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = area.getIdArea();
                if (findArea(id) == null) {
                    throw new NonexistentEntityException("The area with id " + id + " no longer exists.");
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
            Area area;
            try {
                area = em.getReference(Area.class, id);
                area.getIdArea();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The area with id " + id + " no longer exists.", enfe);
            }
            List<Tutor> tutorList = area.getTutorList();
            for (Tutor tutorListTutor : tutorList) {
                tutorListTutor.setIdArea(null);
                tutorListTutor = em.merge(tutorListTutor);
            }
            em.remove(area);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Area> findAreaEntities() {
        return findAreaEntities(true, -1, -1);
    }

    public List<Area> findAreaEntities(int maxResults, int firstResult) {
        return findAreaEntities(false, maxResults, firstResult);
    }

    private List<Area> findAreaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Area.class));
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

    public Area findArea(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Area.class, id);
        } finally {
            em.close();
        }
    }

    public int getAreaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Area> rt = cq.from(Area.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
