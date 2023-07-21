package com.grsc.modelo.daos;

import com.grsc.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JustificacionJpaController implements Serializable {

    public JustificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Justificacion justificacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(justificacion);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Justificacion justificacion) throws Exception {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        em.merge(justificacion);
        em.getTransaction().commit();
        em.close();
    }


    public void destroy(BigInteger id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        Justificacion justificacion = em.find(Justificacion.class, id);
        if (justificacion == null) {
            throw new NonexistentEntityException("No existe la justificacion a borrar. Id=" + justificacion.getIdJustificacion());
        }
        em.remove(justificacion);
        em.getTransaction().commit();

    }
    

    public List<Justificacion> findJustificacionEntities() {
        return findJustificacionEntities(true, -1, -1);
    }

    public List<Justificacion> findJustificacionEntities(int maxResults, int firstResult) {
        return findJustificacionEntities(false, maxResults, firstResult);
    }

    private List<Justificacion> findJustificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Justificacion.class));
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

    public Justificacion findJustificacion(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Justificacion.class, id);
        } finally {
            em.close();
        }
    }
    
    public Justificacion findJustificacionUsuarioHoraEvento(Date fechaHora, Evento idEvento, Estudiante idUsuario){
        EntityManager em = getEntityManager();
        Justificacion justificacionRes = new Justificacion();
        try{
        List<Justificacion> listaResultado = em.createNamedQuery("Justificacion.findByHoraEventoUsuario")
                    .setParameter("fechaHora", fechaHora)
                    .setParameter("idEvento", idEvento)
                    .setParameter("idUsuario", idUsuario)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idJus = listaResultado.get(i).getIdJustificacion();
                    Date fechayHora = listaResultado.get(i).getFechaHora();
                    Evento evento = listaResultado.get(i).getIdEvento();
                    Estudiante estudiante = listaResultado.get(i).getIdUsuario();
                    String detalle = listaResultado.get(i).getDetalle();
                    EstadoPeticion estado = listaResultado.get(i).getIdEstadoPeticion();
                   
                    justificacionRes = Justificacion.builder()
                            .idJustificacion(idJus)
                            .idEvento(evento)
                            .idUsuario(estudiante)
                            .fechaHora(fechayHora)
                            .idEstadoPeticion(estado)
                            .detalle(detalle)
                            .build();
                }
        }
        return justificacionRes;
        }finally {
            em.close();
        }        
    }

    public int getJustificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Justificacion> rt = cq.from(Justificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
