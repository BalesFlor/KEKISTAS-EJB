package com.grsc.modelo.daos;

import com.grsc.modelo.daos.exceptions.IllegalOrphanException;
import com.grsc.modelo.daos.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.TipoEvento;
import com.grsc.modelo.entities.Analista;
import java.util.ArrayList;
import java.util.List;
import com.grsc.modelo.entities.ConvocatoriaAsistencia;
import com.grsc.modelo.entities.Evento;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EventoJpaController implements Serializable {

    public EventoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evento evento) {
        if (evento.getAnalistaList() == null) {
            evento.setAnalistaList(new ArrayList<Analista>());
        }
        if (evento.getConvocatoriaAsistenciaList() == null) {
            evento.setConvocatoriaAsistenciaList(new ArrayList<ConvocatoriaAsistencia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoEvento tipoEvento = evento.getTipoEvento();
            if (tipoEvento != null) {
                tipoEvento = em.getReference(tipoEvento.getClass(), tipoEvento.getId());
                evento.setTipoEvento(tipoEvento);
            }
            List<Analista> attachedAnalistaList = new ArrayList<Analista>();
            for (Analista analistaListAnalistaToAttach : evento.getAnalistaList()) {
                analistaListAnalistaToAttach = em.getReference(analistaListAnalistaToAttach.getClass(), analistaListAnalistaToAttach.getIdUsuario());
                attachedAnalistaList.add(analistaListAnalistaToAttach);
            }
            evento.setAnalistaList(attachedAnalistaList);
            List<ConvocatoriaAsistencia> attachedConvocatoriaAsistenciaList = new ArrayList<ConvocatoriaAsistencia>();
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListConvocatoriaAsistenciaToAttach : evento.getConvocatoriaAsistenciaList()) {
                convocatoriaAsistenciaListConvocatoriaAsistenciaToAttach = em.getReference(convocatoriaAsistenciaListConvocatoriaAsistenciaToAttach.getClass(), convocatoriaAsistenciaListConvocatoriaAsistenciaToAttach.getConvocatoriaAsistenciaPK());
                attachedConvocatoriaAsistenciaList.add(convocatoriaAsistenciaListConvocatoriaAsistenciaToAttach);
            }
            evento.setConvocatoriaAsistenciaList(attachedConvocatoriaAsistenciaList);
            em.persist(evento);
            if (tipoEvento != null) {
                tipoEvento.getEventoList().add(evento);
                tipoEvento = em.merge(tipoEvento);
            }
            for (Analista analistaListAnalista : evento.getAnalistaList()) {
                analistaListAnalista.getEventoList().add(evento);
                analistaListAnalista = em.merge(analistaListAnalista);
            }
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListConvocatoriaAsistencia : evento.getConvocatoriaAsistenciaList()) {
                Evento oldEventoOfConvocatoriaAsistenciaListConvocatoriaAsistencia = convocatoriaAsistenciaListConvocatoriaAsistencia.getEvento();
                convocatoriaAsistenciaListConvocatoriaAsistencia.setEvento(evento);
                convocatoriaAsistenciaListConvocatoriaAsistencia = em.merge(convocatoriaAsistenciaListConvocatoriaAsistencia);
                if (oldEventoOfConvocatoriaAsistenciaListConvocatoriaAsistencia != null) {
                    oldEventoOfConvocatoriaAsistenciaListConvocatoriaAsistencia.getConvocatoriaAsistenciaList().remove(convocatoriaAsistenciaListConvocatoriaAsistencia);
                    oldEventoOfConvocatoriaAsistenciaListConvocatoriaAsistencia = em.merge(oldEventoOfConvocatoriaAsistenciaListConvocatoriaAsistencia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Evento evento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento persistentEvento = em.find(Evento.class, evento.getIdEvento());
            TipoEvento tipoEventoOld = persistentEvento.getTipoEvento();
            TipoEvento tipoEventoNew = evento.getTipoEvento();
            List<Analista> analistaListOld = persistentEvento.getAnalistaList();
            List<Analista> analistaListNew = evento.getAnalistaList();
            List<ConvocatoriaAsistencia> convocatoriaAsistenciaListOld = persistentEvento.getConvocatoriaAsistenciaList();
            List<ConvocatoriaAsistencia> convocatoriaAsistenciaListNew = evento.getConvocatoriaAsistenciaList();
            List<String> illegalOrphanMessages = null;
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListOldConvocatoriaAsistencia : convocatoriaAsistenciaListOld) {
                if (!convocatoriaAsistenciaListNew.contains(convocatoriaAsistenciaListOldConvocatoriaAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConvocatoriaAsistencia " + convocatoriaAsistenciaListOldConvocatoriaAsistencia + " since its evento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoEventoNew != null) {
                tipoEventoNew = em.getReference(tipoEventoNew.getClass(), tipoEventoNew.getId());
                evento.setTipoEvento(tipoEventoNew);
            }
            List<Analista> attachedAnalistaListNew = new ArrayList<Analista>();
            for (Analista analistaListNewAnalistaToAttach : analistaListNew) {
                analistaListNewAnalistaToAttach = em.getReference(analistaListNewAnalistaToAttach.getClass(), analistaListNewAnalistaToAttach.getIdUsuario());
                attachedAnalistaListNew.add(analistaListNewAnalistaToAttach);
            }
            analistaListNew = attachedAnalistaListNew;
            evento.setAnalistaList(analistaListNew);
            List<ConvocatoriaAsistencia> attachedConvocatoriaAsistenciaListNew = new ArrayList<ConvocatoriaAsistencia>();
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach : convocatoriaAsistenciaListNew) {
                convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach = em.getReference(convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach.getClass(), convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach.getConvocatoriaAsistenciaPK());
                attachedConvocatoriaAsistenciaListNew.add(convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach);
            }
            convocatoriaAsistenciaListNew = attachedConvocatoriaAsistenciaListNew;
            evento.setConvocatoriaAsistenciaList(convocatoriaAsistenciaListNew);
            evento = em.merge(evento);
            if (tipoEventoOld != null && !tipoEventoOld.equals(tipoEventoNew)) {
                tipoEventoOld.getEventoList().remove(evento);
                tipoEventoOld = em.merge(tipoEventoOld);
            }
            if (tipoEventoNew != null && !tipoEventoNew.equals(tipoEventoOld)) {
                tipoEventoNew.getEventoList().add(evento);
                tipoEventoNew = em.merge(tipoEventoNew);
            }
            for (Analista analistaListOldAnalista : analistaListOld) {
                if (!analistaListNew.contains(analistaListOldAnalista)) {
                    analistaListOldAnalista.getEventoList().remove(evento);
                    analistaListOldAnalista = em.merge(analistaListOldAnalista);
                }
            }
            for (Analista analistaListNewAnalista : analistaListNew) {
                if (!analistaListOld.contains(analistaListNewAnalista)) {
                    analistaListNewAnalista.getEventoList().add(evento);
                    analistaListNewAnalista = em.merge(analistaListNewAnalista);
                }
            }
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListNewConvocatoriaAsistencia : convocatoriaAsistenciaListNew) {
                if (!convocatoriaAsistenciaListOld.contains(convocatoriaAsistenciaListNewConvocatoriaAsistencia)) {
                    Evento oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia = convocatoriaAsistenciaListNewConvocatoriaAsistencia.getEvento();
                    convocatoriaAsistenciaListNewConvocatoriaAsistencia.setEvento(evento);
                    convocatoriaAsistenciaListNewConvocatoriaAsistencia = em.merge(convocatoriaAsistenciaListNewConvocatoriaAsistencia);
                    if (oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia != null && !oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia.equals(evento)) {
                        oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia.getConvocatoriaAsistenciaList().remove(convocatoriaAsistenciaListNewConvocatoriaAsistencia);
                        oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia = em.merge(oldEventoOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = evento.getIdEvento();
                if (findEvento(id) == null) {
                    throw new NonexistentEntityException("The evento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Evento evento;
            try {
                evento = em.getReference(Evento.class, id);
                evento.getIdEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConvocatoriaAsistencia> convocatoriaAsistenciaListOrphanCheck = evento.getConvocatoriaAsistenciaList();
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListOrphanCheckConvocatoriaAsistencia : convocatoriaAsistenciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Evento (" + evento + ") cannot be destroyed since the ConvocatoriaAsistencia " + convocatoriaAsistenciaListOrphanCheckConvocatoriaAsistencia + " in its convocatoriaAsistenciaList field has a non-nullable evento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoEvento tipoEvento = evento.getTipoEvento();
            if (tipoEvento != null) {
                tipoEvento.getEventoList().remove(evento);
                tipoEvento = em.merge(tipoEvento);
            }
            List<Analista> analistaList = evento.getAnalistaList();
            for (Analista analistaListAnalista : analistaList) {
                analistaListAnalista.getEventoList().remove(evento);
                analistaListAnalista = em.merge(analistaListAnalista);
            }
            em.remove(evento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Evento> findEventoEntities() {
        return findEventoEntities(true, -1, -1);
    }

    public List<Evento> findEventoEntities(int maxResults, int firstResult) {
        return findEventoEntities(false, maxResults, firstResult);
    }

    private List<Evento> findEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evento.class));
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

    public Evento findEvento(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }
    
    public Evento findEvento(Date fechaHoraInicio, String titulo) {
        EntityManager em = getEntityManager();
        Evento eventoRes = new Evento();
        try{
        List<Evento> listaResultado = em.createNamedQuery("Evento.findByFechasTitulo")
                    .setParameter("fechaHoraInicio", fechaHoraInicio)
                    .setParameter("titulo", titulo)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idEvento = listaResultado.get(i).getIdEvento();
                    Date fechaHInicio = listaResultado.get(i).getFechaHoraInicio();
                    Date fechaHFin = listaResultado.get(i).getFechaHoraFin();
                    String tit = listaResultado.get(i).getTitulo();
                    TipoEvento tipoEvento = listaResultado.get(i).getTipoEvento();
                    
                    
                    eventoRes = Evento.builder()
                            .idEvento(idEvento)
                            .fechaHoraInicio(fechaHInicio)
                            .fechaHoraFin(fechaHFin)
                            .titulo(tit)
                            .tipoEvento(tipoEvento)
                            .build();
                }
        }
        return eventoRes;
        }finally {
            em.close();
        }        
    }

    public Evento findEvento(String titulo) {
        EntityManager em = getEntityManager();
        Evento eventoRes = new Evento();
        try{
        List<Evento> listaResultado = em.createNamedQuery("Evento.findByTitulo")
                    .setParameter("titulo", titulo)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger idEvento = listaResultado.get(i).getIdEvento();
                    Date fechaHInicio = listaResultado.get(i).getFechaHoraInicio();
                    Date fechaHFin = listaResultado.get(i).getFechaHoraFin();
                    String tit = listaResultado.get(i).getTitulo();
                    TipoEvento tipoEvento = listaResultado.get(i).getTipoEvento();
                    
                    
                    eventoRes = Evento.builder()
                            .idEvento(idEvento)
                            .fechaHoraInicio(fechaHInicio)
                            .fechaHoraFin(fechaHFin)
                            .titulo(tit)
                            .tipoEvento(tipoEvento)
                            .build();
                }
        }
        return eventoRes;
        }finally {
            em.close();
        }        
    }
    
    public int getEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evento> rt = cq.from(Evento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
