package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.AccionJustificacion;
import com.grsc.modelo.entities.AccionJustificacionPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Justificacion;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AccionJustificacionJpaController implements Serializable {

    public AccionJustificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccionJustificacion accionJustificacion) throws PreexistingEntityException, Exception {
         EntityManager em = null;
    
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(accionJustificacion);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccionJustificacion accionJustificacion) throws NonexistentEntityException, Exception {
        accionJustificacion.getAccionJustificacionPK().setIdJustificacion(accionJustificacion.getJustificacion().getIdJustificacion());
        accionJustificacion.getAccionJustificacionPK().setIdUsuario(accionJustificacion.getAnalista().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionJustificacion persistentAccionJustificacion = em.find(AccionJustificacion.class, accionJustificacion.getAccionJustificacionPK());
            Analista analistaOld = persistentAccionJustificacion.getAnalista();
            Analista analistaNew = accionJustificacion.getAnalista();
            Justificacion justificacionOld = persistentAccionJustificacion.getJustificacion();
            Justificacion justificacionNew = accionJustificacion.getJustificacion();
            if (analistaNew != null) {
                analistaNew = em.getReference(analistaNew.getClass(), analistaNew.getIdUsuario());
                accionJustificacion.setAnalista(analistaNew);
            }
            if (justificacionNew != null) {
                justificacionNew = em.getReference(justificacionNew.getClass(), justificacionNew.getIdJustificacion());
                accionJustificacion.setJustificacion(justificacionNew);
            }
            accionJustificacion = em.merge(accionJustificacion);
            if (analistaOld != null && !analistaOld.equals(analistaNew)) {
                analistaOld.getAccionJustificacionList().remove(accionJustificacion);
                analistaOld = em.merge(analistaOld);
            }
            if (analistaNew != null && !analistaNew.equals(analistaOld)) {
                analistaNew.getAccionJustificacionList().add(accionJustificacion);
                analistaNew = em.merge(analistaNew);
            }
            if (justificacionOld != null && !justificacionOld.equals(justificacionNew)) {
                justificacionOld.getAccionJustificacionList().remove(accionJustificacion);
                justificacionOld = em.merge(justificacionOld);
            }
            if (justificacionNew != null && !justificacionNew.equals(justificacionOld)) {
                justificacionNew.getAccionJustificacionList().add(accionJustificacion);
                justificacionNew = em.merge(justificacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AccionJustificacionPK id = accionJustificacion.getAccionJustificacionPK();
                if (findAccionJustificacion(id) == null) {
                    throw new NonexistentEntityException("The accionJustificacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AccionJustificacionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionJustificacion accionJustificacion;
            try {
                accionJustificacion = em.getReference(AccionJustificacion.class, id);
                accionJustificacion.getAccionJustificacionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accionJustificacion with id " + id + " no longer exists.", enfe);
            }
            Analista analista = accionJustificacion.getAnalista();
            if (analista != null) {
                analista.getAccionJustificacionList().remove(accionJustificacion);
                analista = em.merge(analista);
            }
            Justificacion justificacion = accionJustificacion.getJustificacion();
            if (justificacion != null) {
                justificacion.getAccionJustificacionList().remove(accionJustificacion);
                justificacion = em.merge(justificacion);
            }
            em.remove(accionJustificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccionJustificacion> findAccionJustificacionEntities() {
        return findAccionJustificacionEntities(true, -1, -1);
    }

    public List<AccionJustificacion> findAccionJustificacionEntities(int maxResults, int firstResult) {
        return findAccionJustificacionEntities(false, maxResults, firstResult);
    }

    private List<AccionJustificacion> findAccionJustificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccionJustificacion.class));
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

    public AccionJustificacion findAccionJustificacion(AccionJustificacionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccionJustificacion.class, id);
        } finally {
            em.close();
        }
    }
    
    public AccionJustificacion findAccionJustificacion(Analista analista, Date fechaHora) {
        EntityManager em = getEntityManager();
        AccionJustificacion accJusRes = new AccionJustificacion();
        try {
            List<AccionJustificacion> listaResultado = em.createNamedQuery("AccionJustificacion.findByIdUsuarioFechaHora")
                    .setParameter("idUsuario", analista)
                    .setParameter("fechaHora", fechaHora)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    AccionJustificacionPK accJusPK = listaResultado.get(i).getAccionJustificacionPK();
                    String detalle = listaResultado.get(i).getDetalle();
                    Date fecha = listaResultado.get(i).getFechaHora();
                    Analista analistaAcc = listaResultado.get(i).getAnalista();

                    accJusRes = AccionJustificacion.builder()
                            .accionJustificacionPK(accJusPK)
                            .detalle(detalle)
                            .fechaHora(fecha)
                            .analista(analistaAcc)
                            .build();
                }
            }
            return accJusRes;
        } finally {
            em.close();
        }
    }

     public AccionJustificacion findAccionJustificacion(Justificacion justificacion, Analista analista) {
        EntityManager em = getEntityManager();
        AccionJustificacion accJusRes = new AccionJustificacion();
        try {
            List<AccionJustificacion> listaResultado = em.createNamedQuery("AccionJustificacion.findByIdUsuarioIdJustificacion")
                    .setParameter("idUsuario", analista.getIdUsuario())
                    .setParameter("idJustificacion", justificacion.getIdJustificacion())
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    AccionJustificacionPK accJusPK = listaResultado.get(i).getAccionJustificacionPK();
                    String detalle = listaResultado.get(i).getDetalle();
                    Date fecha = listaResultado.get(i).getFechaHora();
                    Analista analistaAcc = listaResultado.get(i).getAnalista();

                    accJusRes = AccionJustificacion.builder()
                            .accionJustificacionPK(accJusPK)
                            .detalle(detalle)
                            .fechaHora(fecha)
                            .analista(analistaAcc)
                            .build();
                }
            }
            return accJusRes;
        } finally {
            em.close();
        }
    }


    public int getAccionJustificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccionJustificacion> rt = cq.from(AccionJustificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
