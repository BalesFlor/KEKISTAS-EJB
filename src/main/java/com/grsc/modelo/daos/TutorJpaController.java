package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Area;
import com.grsc.modelo.entities.TipoTutor;
import com.grsc.modelo.entities.Tutor;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TutorJpaController implements Serializable {

    public TutorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tutor tutor) throws IllegalOrphanException, PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.merge(tutor);
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tutor tutor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tutor persistentTutor = em.find(Tutor.class, tutor.getIdUsuario());
            Area idAreaOld = persistentTutor.getIdArea();
            Area idAreaNew = tutor.getIdArea();
            TipoTutor idTipoTutorOld = persistentTutor.getIdTipoTutor();
            TipoTutor idTipoTutorNew = tutor.getIdTipoTutor();
            Usuarios usuariosOld = persistentTutor.getUsuarios();
            Usuarios usuariosNew = tutor.getUsuarios();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Tutor oldTutorOfUsuarios = usuariosNew.getTutor();
                if (oldTutorOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Tutor whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAreaNew != null) {
                idAreaNew = em.getReference(idAreaNew.getClass(), idAreaNew.getIdArea());
                tutor.setIdArea(idAreaNew);
            }
            if (idTipoTutorNew != null) {
                idTipoTutorNew = em.getReference(idTipoTutorNew.getClass(), idTipoTutorNew.getIdTipoTutor());
                tutor.setIdTipoTutor(idTipoTutorNew);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getIdUsuario());
                tutor.setUsuarios(usuariosNew);
            }
            tutor = em.merge(tutor);
            if (idAreaOld != null && !idAreaOld.equals(idAreaNew)) {
                idAreaOld.getTutorList().remove(tutor);
                idAreaOld = em.merge(idAreaOld);
            }
            if (idAreaNew != null && !idAreaNew.equals(idAreaOld)) {
                idAreaNew.getTutorList().add(tutor);
                idAreaNew = em.merge(idAreaNew);
            }
            if (idTipoTutorOld != null && !idTipoTutorOld.equals(idTipoTutorNew)) {
                idTipoTutorOld.getTutorList().remove(tutor);
                idTipoTutorOld = em.merge(idTipoTutorOld);
            }
            if (idTipoTutorNew != null && !idTipoTutorNew.equals(idTipoTutorOld)) {
                idTipoTutorNew.getTutorList().add(tutor);
                idTipoTutorNew = em.merge(idTipoTutorNew);
            }
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setTutor(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setTutor(tutor);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = tutor.getIdUsuario();
                if (findTutor(id) == null) {
                    throw new NonexistentEntityException("The tutor with id " + id + " no longer exists.");
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
            Tutor tutor;
            try {
                tutor = em.getReference(Tutor.class, id);
                tutor.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tutor with id " + id + " no longer exists.", enfe);
            }
            Area idArea = tutor.getIdArea();
            if (idArea != null) {
                idArea.getTutorList().remove(tutor);
                idArea = em.merge(idArea);
            }
            TipoTutor idTipoTutor = tutor.getIdTipoTutor();
            if (idTipoTutor != null) {
                idTipoTutor.getTutorList().remove(tutor);
                idTipoTutor = em.merge(idTipoTutor);
            }
            Usuarios usuarios = tutor.getUsuarios();
            if (usuarios != null) {
                usuarios.setTutor(null);
                usuarios = em.merge(usuarios);
            }
            em.remove(tutor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tutor> findTutorEntities() {
        return findTutorEntities(true, -1, -1);
    }

    public List<Tutor> findTutorEntities(int maxResults, int firstResult) {
        return findTutorEntities(false, maxResults, firstResult);
    }

    private List<Tutor> findTutorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tutor.class));
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

    public Tutor findTutor(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tutor.class, id);
        } finally {
            em.close();
        }
    }

    public int getTutorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tutor> rt = cq.from(Tutor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
