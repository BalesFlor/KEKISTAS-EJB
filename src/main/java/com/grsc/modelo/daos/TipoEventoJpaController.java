/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grsc.modelo.daos;

import com.grsc.modelo.daos.exceptions.NonexistentEntityException;
import com.grsc.modelo.daos.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.TipoEvento;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author fbale
 */
public class TipoEventoJpaController implements Serializable {

    public TipoEventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoEvento tipoEvento) throws PreexistingEntityException, Exception {
        if (tipoEvento.getEventoList() == null) {
            tipoEvento.setEventoList(new ArrayList<Evento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Evento> attachedEventoList = new ArrayList<Evento>();
            for (Evento eventoListEventoToAttach : tipoEvento.getEventoList()) {
                eventoListEventoToAttach = em.getReference(eventoListEventoToAttach.getClass(), eventoListEventoToAttach.getIdEvento());
                attachedEventoList.add(eventoListEventoToAttach);
            }
            tipoEvento.setEventoList(attachedEventoList);
            em.persist(tipoEvento);
            for (Evento eventoListEvento : tipoEvento.getEventoList()) {
                TipoEvento oldTipoEventoOfEventoListEvento = eventoListEvento.getTipoEvento();
                eventoListEvento.setTipoEvento(tipoEvento);
                eventoListEvento = em.merge(eventoListEvento);
                if (oldTipoEventoOfEventoListEvento != null) {
                    oldTipoEventoOfEventoListEvento.getEventoList().remove(eventoListEvento);
                    oldTipoEventoOfEventoListEvento = em.merge(oldTipoEventoOfEventoListEvento);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoEvento(tipoEvento.getId()) != null) {
                throw new PreexistingEntityException("TipoEvento " + tipoEvento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoEvento tipoEvento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento persistentTipoEvento = em.find(TipoEvento.class, tipoEvento.getId());
            List<Evento> eventoListOld = persistentTipoEvento.getEventoList();
            List<Evento> eventoListNew = tipoEvento.getEventoList();
            List<Evento> attachedEventoListNew = new ArrayList<Evento>();
            for (Evento eventoListNewEventoToAttach : eventoListNew) {
                eventoListNewEventoToAttach = em.getReference(eventoListNewEventoToAttach.getClass(), eventoListNewEventoToAttach.getIdEvento());
                attachedEventoListNew.add(eventoListNewEventoToAttach);
            }
            eventoListNew = attachedEventoListNew;
            tipoEvento.setEventoList(eventoListNew);
            tipoEvento = em.merge(tipoEvento);
            for (Evento eventoListOldEvento : eventoListOld) {
                if (!eventoListNew.contains(eventoListOldEvento)) {
                    eventoListOldEvento.setTipoEvento(null);
                    eventoListOldEvento = em.merge(eventoListOldEvento);
                }
            }
            for (Evento eventoListNewEvento : eventoListNew) {
                if (!eventoListOld.contains(eventoListNewEvento)) {
                    TipoEvento oldTipoEventoOfEventoListNewEvento = eventoListNewEvento.getTipoEvento();
                    eventoListNewEvento.setTipoEvento(tipoEvento);
                    eventoListNewEvento = em.merge(eventoListNewEvento);
                    if (oldTipoEventoOfEventoListNewEvento != null && !oldTipoEventoOfEventoListNewEvento.equals(tipoEvento)) {
                        oldTipoEventoOfEventoListNewEvento.getEventoList().remove(eventoListNewEvento);
                        oldTipoEventoOfEventoListNewEvento = em.merge(oldTipoEventoOfEventoListNewEvento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = tipoEvento.getId();
                if (findTipoEvento(id) == null) {
                    throw new NonexistentEntityException("The tipoEvento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento tipoEvento;
            try {
                tipoEvento = em.getReference(TipoEvento.class, id);
                tipoEvento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoEvento with id " + id + " no longer exists.", enfe);
            }
            List<Evento> eventoList = tipoEvento.getEventoList();
            for (Evento eventoListEvento : eventoList) {
                eventoListEvento.setTipoEvento(null);
                eventoListEvento = em.merge(eventoListEvento);
            }
            em.remove(tipoEvento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoEvento> findTipoEventoEntities() {
        return findTipoEventoEntities(true, -1, -1);
    }

    public List<TipoEvento> findTipoEventoEntities(int maxResults, int firstResult) {
        return findTipoEventoEntities(false, maxResults, firstResult);
    }

    private List<TipoEvento> findTipoEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoEvento.class));
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

    public TipoEvento findTipoEvento(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoEvento.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoEvento> rt = cq.from(TipoEvento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
