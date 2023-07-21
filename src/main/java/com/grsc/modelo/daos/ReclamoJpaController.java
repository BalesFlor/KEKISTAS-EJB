package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.EstadoPeticion;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Reclamo;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ReclamoJpaController implements Serializable {

    public ReclamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reclamo reclamo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.persist(reclamo);
            em.flush();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

     public void edit(Reclamo reclamo) throws Exception {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        em.merge(reclamo);
        em.getTransaction().commit();
        em.close();
    }


    public void destroy(BigInteger id) throws NonexistentEntityException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Reclamo reclamo = em.find(Reclamo.class, id);
        
        if (reclamo == null) {
            throw new NonexistentEntityException("No existe la reclamo a borrar. Id=" + id);
        }else{
            em.remove(reclamo);
            em.getTransaction().commit();
        }
    }


    public List<Reclamo> findReclamoEntities() {
        return findReclamoEntities(true, -1, -1);
    }

    public List<Reclamo> findReclamoEntities(int maxResults, int firstResult) {
        return findReclamoEntities(false, maxResults, firstResult);
    }

    private List<Reclamo> findReclamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reclamo.class));
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

    public Reclamo findReclamo(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reclamo.class, id);
        } finally {
            em.close();
        }
    }

    public Reclamo buscarReclamo(Estudiante est, String tituloR) {
        EntityManager em = getEntityManager();
        Reclamo reclamoRes = new Reclamo();
        try{
        List<Reclamo> listaResultado = em.createNamedQuery("Reclamo.findByIdUsuarioTitulo")
                    .setParameter("idUsuario", est)
                    .setParameter("titulo", tituloR)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idReclamo = listaResultado.get(i).getIdReclamo();
                    Date fechaDia = listaResultado.get(i).getFecha();
                    Date fechaHora = listaResultado.get(i).getFechaHora();
                    String titulo = listaResultado.get(i).getTitulo();
                    String detalle = listaResultado.get(i).getDetalle();
                    BigInteger semestre = listaResultado.get(i).getSemestre();
                    BigInteger creditos = listaResultado.get(i).getCreditos();
                    EstadoPeticion estadoPeticion = listaResultado.get(i).getIdEstadoPeticion();
                    Evento evento = listaResultado.get(i).getIdEvento();
                    Estudiante estudiante = listaResultado.get(i).getIdUsuario();
                    
                    reclamoRes = Reclamo.builder()
                            .idReclamo(idReclamo)
                            .fecha(fechaDia)
                            .fechaHora(fechaHora)
                            .titulo(titulo)
                            .detalle(detalle)
                            .semestre(semestre)
                            .creditos(creditos)
                            .idEstadoPeticion(estadoPeticion)
                            .idEvento(evento)
                            .idUsuario(estudiante)
                            .build();
                }
        }
        return reclamoRes;
        }finally {
            em.close();
        }        
    }
    
    public int getReclamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reclamo> rt = cq.from(Reclamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public Reclamo findReclamoUsuarioHoraEvento(Date fechaHora, Evento idEvento, Estudiante idUsuario){
        EntityManager em = getEntityManager();
        Reclamo reclamoRes = new Reclamo();
        try{
        List<Reclamo> listaResultado = em.createNamedQuery("Reclamo.findByHoraEventoUsuario")
                    .setParameter("fechaHora", fechaHora)
                    .setParameter("idEvento", idEvento)
                    .setParameter("idUsuario", idUsuario)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idJus = listaResultado.get(i).getIdReclamo();
                    Date fechayHora = listaResultado.get(i).getFechaHora();
                    Evento evento = listaResultado.get(i).getIdEvento();
                    Estudiante estudiante = listaResultado.get(i).getIdUsuario();
                    String detalle = listaResultado.get(i).getDetalle();
                    EstadoPeticion estado = listaResultado.get(i).getIdEstadoPeticion();
                    
                    
                    reclamoRes = Reclamo.builder()
                            .idReclamo(idJus)
                            .idEvento(evento)
                            .idUsuario(estudiante)
                            .fechaHora(fechayHora)
                            .idEstadoPeticion(estado)
                            .detalle(detalle)
                            .build();
                }
        }
        return reclamoRes;
        }finally {
            em.close();
        }        
    }

}
