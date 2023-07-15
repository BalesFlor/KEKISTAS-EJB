package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.AccionReclamo;
import com.grsc.modelo.entities.AccionReclamoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Analista;
import com.grsc.modelo.entities.Reclamo;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AccionReclamoJpaController implements Serializable {

    public AccionReclamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccionReclamo accionReclamo) throws PreexistingEntityException, Exception {
    EntityManager em = null;
    
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(accionReclamo);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public BigInteger obtenerId() {
        String query = "SELECT ACC_REC_SEQ.nextVal() FROM dual";

        EntityManager entityManager = getEntityManager();
        Object result = entityManager.createNativeQuery(query).getSingleResult();
        BigInteger nextVal = (BigInteger) result;
        return nextVal;
    }
    
    public void edit(AccionReclamo accionReclamo) throws NonexistentEntityException, Exception {
        accionReclamo.getAccionReclamoPK().setIdReclamo(accionReclamo.getReclamo().getIdReclamo());
        accionReclamo.getAccionReclamoPK().setIdUsuario(accionReclamo.getAnalista().getIdUsuario());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionReclamo persistentAccionReclamo = em.find(AccionReclamo.class, accionReclamo.getAccionReclamoPK());
            Analista analistaOld = persistentAccionReclamo.getAnalista();
            Analista analistaNew = accionReclamo.getAnalista();
            Reclamo reclamoOld = persistentAccionReclamo.getReclamo();
            Reclamo reclamoNew = accionReclamo.getReclamo();
            if (analistaNew != null) {
                analistaNew = em.getReference(analistaNew.getClass(), analistaNew.getIdUsuario());
                accionReclamo.setAnalista(analistaNew);
            }
            if (reclamoNew != null) {
                reclamoNew = em.getReference(reclamoNew.getClass(), reclamoNew.getIdReclamo());
                accionReclamo.setReclamo(reclamoNew);
            }
            accionReclamo = em.merge(accionReclamo);
            if (analistaOld != null && !analistaOld.equals(analistaNew)) {
                analistaOld.getAccionReclamoList().remove(accionReclamo);
                analistaOld = em.merge(analistaOld);
            }
            if (analistaNew != null && !analistaNew.equals(analistaOld)) {
                analistaNew.getAccionReclamoList().add(accionReclamo);
                analistaNew = em.merge(analistaNew);
            }
            if (reclamoOld != null && !reclamoOld.equals(reclamoNew)) {
                reclamoOld.getAccionReclamoList().remove(accionReclamo);
                reclamoOld = em.merge(reclamoOld);
            }
            if (reclamoNew != null && !reclamoNew.equals(reclamoOld)) {
                reclamoNew.getAccionReclamoList().add(accionReclamo);
                reclamoNew = em.merge(reclamoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AccionReclamoPK id = accionReclamo.getAccionReclamoPK();
                if (findAccionReclamo(id) == null) {
                    throw new NonexistentEntityException("The accionReclamo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AccionReclamoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccionReclamo accionReclamo;
            try {
                accionReclamo = em.getReference(AccionReclamo.class, id);
                accionReclamo.getAccionReclamoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accionReclamo with id " + id + " no longer exists.", enfe);
            }
            Analista analista = accionReclamo.getAnalista();
            if (analista != null) {
                analista.getAccionReclamoList().remove(accionReclamo);
                analista = em.merge(analista);
            }
            Reclamo reclamo = accionReclamo.getReclamo();
            if (reclamo != null) {
                reclamo.getAccionReclamoList().remove(accionReclamo);
                reclamo = em.merge(reclamo);
            }
            em.remove(accionReclamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccionReclamo> findAccionReclamoEntities() {
        return findAccionReclamoEntities(true, -1, -1);
    }

    public List<AccionReclamo> findAccionReclamoEntities(int maxResults, int firstResult) {
        return findAccionReclamoEntities(false, maxResults, firstResult);
    }

    private List<AccionReclamo> findAccionReclamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccionReclamo.class));
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

    public AccionReclamo findAccionReclamo(AccionReclamoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccionReclamo.class, id);
        } finally {
            em.close();
        }
    }
    
    public AccionReclamo findAccionReclamo(Reclamo reclamo, Analista analista) {
        EntityManager em = getEntityManager();
        AccionReclamo accRecRes = new AccionReclamo();
        try {
            List<AccionReclamo> listaResultado = em.createNamedQuery("AccionReclamo.findByIdUsuarioIdReclamo")
                    .setParameter("idUsuario", analista.getIdUsuario())
                    .setParameter("idReclamo", reclamo.getIdReclamo())
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    AccionReclamoPK accRecPK = listaResultado.get(i).getAccionReclamoPK();
                    String detalle = listaResultado.get(i).getDetalle();
                    Date fecha = listaResultado.get(i).getFechaHora();
                    Analista analistaAcc = listaResultado.get(i).getAnalista();

                    accRecRes = AccionReclamo.builder()
                            .accionReclamoPK(accRecPK)
                            .detalle(detalle)
                            .fechaHora(fecha)
                            .analista(analistaAcc)
                            .build();
                }
            }
            return accRecRes;
        } finally {
            em.close();
        }
    }

    public int getAccionReclamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccionReclamo> rt = cq.from(AccionReclamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
