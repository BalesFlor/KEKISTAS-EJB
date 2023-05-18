package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.ConvocatoriaAsistencia;
import com.grsc.modelo.entities.ConvocatoriaAsistenciaPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Evento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ConvocatoriaAsistenciaJpaController implements Serializable {

    public ConvocatoriaAsistenciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConvocatoriaAsistencia convocatoriaAsistencia) throws PreexistingEntityException, Exception {
        if (convocatoriaAsistencia.getConvocatoriaAsistenciaPK() == null) {
            convocatoriaAsistencia.setConvocatoriaAsistenciaPK(new ConvocatoriaAsistenciaPK());
        }
        convocatoriaAsistencia.getConvocatoriaAsistenciaPK().setIdEvento(convocatoriaAsistencia.getEvento().getIdEvento());
        convocatoriaAsistencia.getConvocatoriaAsistenciaPK().setIdUsuario(convocatoriaAsistencia.getEstudiante().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante = convocatoriaAsistencia.getEstudiante();
            if (estudiante != null) {
                estudiante = em.getReference(estudiante.getClass(), estudiante.getIdUsuario());
                convocatoriaAsistencia.setEstudiante(estudiante);
            }
            Evento evento = convocatoriaAsistencia.getEvento();
            if (evento != null) {
                evento = em.getReference(evento.getClass(), evento.getIdEvento());
                convocatoriaAsistencia.setEvento(evento);
            }
            em.persist(convocatoriaAsistencia);
            if (estudiante != null) {
                estudiante.getConvocatoriaAsistenciaList().add(convocatoriaAsistencia);
                estudiante = em.merge(estudiante);
            }
            if (evento != null) {
                evento.getConvocatoriaAsistenciaList().add(convocatoriaAsistencia);
                evento = em.merge(evento);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConvocatoriaAsistencia(convocatoriaAsistencia.getConvocatoriaAsistenciaPK()) != null) {
                throw new PreexistingEntityException("ConvocatoriaAsistencia " + convocatoriaAsistencia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConvocatoriaAsistencia convocatoriaAsistencia) throws NonexistentEntityException, Exception {
        convocatoriaAsistencia.getConvocatoriaAsistenciaPK().setIdEvento(convocatoriaAsistencia.getEvento().getIdEvento());
        convocatoriaAsistencia.getConvocatoriaAsistenciaPK().setIdUsuario(convocatoriaAsistencia.getEstudiante().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConvocatoriaAsistencia persistentConvocatoriaAsistencia = em.find(ConvocatoriaAsistencia.class, convocatoriaAsistencia.getConvocatoriaAsistenciaPK());
            Estudiante estudianteOld = persistentConvocatoriaAsistencia.getEstudiante();
            Estudiante estudianteNew = convocatoriaAsistencia.getEstudiante();
            Evento eventoOld = persistentConvocatoriaAsistencia.getEvento();
            Evento eventoNew = convocatoriaAsistencia.getEvento();
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getIdUsuario());
                convocatoriaAsistencia.setEstudiante(estudianteNew);
            }
            if (eventoNew != null) {
                eventoNew = em.getReference(eventoNew.getClass(), eventoNew.getIdEvento());
                convocatoriaAsistencia.setEvento(eventoNew);
            }
            convocatoriaAsistencia = em.merge(convocatoriaAsistencia);
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                estudianteOld.getConvocatoriaAsistenciaList().remove(convocatoriaAsistencia);
                estudianteOld = em.merge(estudianteOld);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                estudianteNew.getConvocatoriaAsistenciaList().add(convocatoriaAsistencia);
                estudianteNew = em.merge(estudianteNew);
            }
            if (eventoOld != null && !eventoOld.equals(eventoNew)) {
                eventoOld.getConvocatoriaAsistenciaList().remove(convocatoriaAsistencia);
                eventoOld = em.merge(eventoOld);
            }
            if (eventoNew != null && !eventoNew.equals(eventoOld)) {
                eventoNew.getConvocatoriaAsistenciaList().add(convocatoriaAsistencia);
                eventoNew = em.merge(eventoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ConvocatoriaAsistenciaPK id = convocatoriaAsistencia.getConvocatoriaAsistenciaPK();
                if (findConvocatoriaAsistencia(id) == null) {
                    throw new NonexistentEntityException("The convocatoriaAsistencia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ConvocatoriaAsistenciaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConvocatoriaAsistencia convocatoriaAsistencia;
            try {
                convocatoriaAsistencia = em.getReference(ConvocatoriaAsistencia.class, id);
                convocatoriaAsistencia.getConvocatoriaAsistenciaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The convocatoriaAsistencia with id " + id + " no longer exists.", enfe);
            }
            Estudiante estudiante = convocatoriaAsistencia.getEstudiante();
            if (estudiante != null) {
                estudiante.getConvocatoriaAsistenciaList().remove(convocatoriaAsistencia);
                estudiante = em.merge(estudiante);
            }
            Evento evento = convocatoriaAsistencia.getEvento();
            if (evento != null) {
                evento.getConvocatoriaAsistenciaList().remove(convocatoriaAsistencia);
                evento = em.merge(evento);
            }
            em.remove(convocatoriaAsistencia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConvocatoriaAsistencia> findConvocatoriaAsistenciaEntities() {
        return findConvocatoriaAsistenciaEntities(true, -1, -1);
    }

    public List<ConvocatoriaAsistencia> findConvocatoriaAsistenciaEntities(int maxResults, int firstResult) {
        return findConvocatoriaAsistenciaEntities(false, maxResults, firstResult);
    }

    private List<ConvocatoriaAsistencia> findConvocatoriaAsistenciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConvocatoriaAsistencia.class));
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

    public ConvocatoriaAsistencia findConvocatoriaAsistencia(ConvocatoriaAsistenciaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConvocatoriaAsistencia.class, id);
        } finally {
            em.close();
        }
    }

    public int getConvocatoriaAsistenciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConvocatoriaAsistencia> rt = cq.from(ConvocatoriaAsistencia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
