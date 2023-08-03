package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Departamento;
import com.grsc.modelo.entities.EstadoItr;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.TipoEvento;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ItrJpaController implements Serializable {

    public ItrJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Itr itr) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(itr);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Itr itr) throws IllegalOrphanException, NonexistentEntityException, Exception {
       EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(itr);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }


    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        Itr itr = em.find(Itr.class, id);
        if (itr == null) {
            throw new NonexistentEntityException("No existe la itr a borrar. Id=" + itr.getIdItr());
        }
        em.remove(itr);
        em.getTransaction().commit();

    }

    public List<Itr> findItrEntities() {
        return findItrEntities(true, -1, -1);
    }

    public List<Itr> findItrEntities(int maxResults, int firstResult) {
        return findItrEntities(false, maxResults, firstResult);
    }

    private List<Itr> findItrEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Itr.class));
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

    public Itr findItr(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Itr.class, id);
        } finally {
            em.close();
        }
    }

    public int getItrCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Itr> rt = cq.from(Itr.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Itr findItr(String nombre) {
        EntityManager em = getEntityManager();
        Itr itrRes = new Itr();
        try{
        List<Itr> listaResultado = em.createNamedQuery("Itr.findByNomItr")
                    .setParameter("nomItr", nombre)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idItr = listaResultado.get(i).getIdItr();               
                    String nomItr = listaResultado.get(i).getNomItr();
                    Departamento departamento = listaResultado.get(i).getIdDepartamento();
                    EstadoItr estado = listaResultado.get(i).getIdEstado();
                    
                    itrRes = Itr.builder()
                            .idItr(idItr)
                            .nomItr(nomItr)
                            .idEstado(estado)
                            .idDepartamento(departamento)
                            .build();
                }
        }
        return itrRes;
        }finally {
            em.close();
        }        
    }
    
}
