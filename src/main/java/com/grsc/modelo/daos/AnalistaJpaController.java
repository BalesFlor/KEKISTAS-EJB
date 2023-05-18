package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Usuarios;
import com.grsc.modelo.entities.Evento;
import java.util.ArrayList;
import java.util.List;
import com.grsc.modelo.entities.AccionConstancia;
import com.grsc.modelo.entities.AccionReclamo;
import com.grsc.modelo.entities.AccionJustificacion;
import com.grsc.modelo.entities.Analista;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class AnalistaJpaController implements Serializable {

    public AnalistaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Analista analista) throws IllegalOrphanException, PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.merge(analista);
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Analista analista) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Analista persistentAnalista = em.find(Analista.class, analista.getIdUsuario());
            Usuarios usuariosOld = persistentAnalista.getUsuarios();
            Usuarios usuariosNew = analista.getUsuarios();
            List<Evento> eventoListOld = persistentAnalista.getEventoList();
            List<Evento> eventoListNew = analista.getEventoList();
            List<AccionConstancia> accionConstanciaListOld = persistentAnalista.getAccionConstanciaList();
            List<AccionConstancia> accionConstanciaListNew = analista.getAccionConstanciaList();
            List<AccionReclamo> accionReclamoListOld = persistentAnalista.getAccionReclamoList();
            List<AccionReclamo> accionReclamoListNew = analista.getAccionReclamoList();
            List<AccionJustificacion> accionJustificacionListOld = persistentAnalista.getAccionJustificacionList();
            List<AccionJustificacion> accionJustificacionListNew = analista.getAccionJustificacionList();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Analista oldAnalistaOfUsuarios = usuariosNew.getAnalista();
                if (oldAnalistaOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Analista whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            for (AccionConstancia accionConstanciaListOldAccionConstancia : accionConstanciaListOld) {
                if (!accionConstanciaListNew.contains(accionConstanciaListOldAccionConstancia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionConstancia " + accionConstanciaListOldAccionConstancia + " since its analista field is not nullable.");
                }
            }
            for (AccionReclamo accionReclamoListOldAccionReclamo : accionReclamoListOld) {
                if (!accionReclamoListNew.contains(accionReclamoListOldAccionReclamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionReclamo " + accionReclamoListOldAccionReclamo + " since its analista field is not nullable.");
                }
            }
            for (AccionJustificacion accionJustificacionListOldAccionJustificacion : accionJustificacionListOld) {
                if (!accionJustificacionListNew.contains(accionJustificacionListOldAccionJustificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionJustificacion " + accionJustificacionListOldAccionJustificacion + " since its analista field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getIdUsuario());
                analista.setUsuarios(usuariosNew);
            }
            List<Evento> attachedEventoListNew = new ArrayList<Evento>();
            for (Evento eventoListNewEventoToAttach : eventoListNew) {
                eventoListNewEventoToAttach = em.getReference(eventoListNewEventoToAttach.getClass(), eventoListNewEventoToAttach.getIdEvento());
                attachedEventoListNew.add(eventoListNewEventoToAttach);
            }
            eventoListNew = attachedEventoListNew;
            analista.setEventoList(eventoListNew);
            List<AccionConstancia> attachedAccionConstanciaListNew = new ArrayList<AccionConstancia>();
            for (AccionConstancia accionConstanciaListNewAccionConstanciaToAttach : accionConstanciaListNew) {
                accionConstanciaListNewAccionConstanciaToAttach = em.getReference(accionConstanciaListNewAccionConstanciaToAttach.getClass(), accionConstanciaListNewAccionConstanciaToAttach.getAccionConstanciaPK());
                attachedAccionConstanciaListNew.add(accionConstanciaListNewAccionConstanciaToAttach);
            }
            accionConstanciaListNew = attachedAccionConstanciaListNew;
            analista.setAccionConstanciaList(accionConstanciaListNew);
            List<AccionReclamo> attachedAccionReclamoListNew = new ArrayList<AccionReclamo>();
            for (AccionReclamo accionReclamoListNewAccionReclamoToAttach : accionReclamoListNew) {
                accionReclamoListNewAccionReclamoToAttach = em.getReference(accionReclamoListNewAccionReclamoToAttach.getClass(), accionReclamoListNewAccionReclamoToAttach.getAccionReclamoPK());
                attachedAccionReclamoListNew.add(accionReclamoListNewAccionReclamoToAttach);
            }
            accionReclamoListNew = attachedAccionReclamoListNew;
            analista.setAccionReclamoList(accionReclamoListNew);
            List<AccionJustificacion> attachedAccionJustificacionListNew = new ArrayList<AccionJustificacion>();
            for (AccionJustificacion accionJustificacionListNewAccionJustificacionToAttach : accionJustificacionListNew) {
                accionJustificacionListNewAccionJustificacionToAttach = em.getReference(accionJustificacionListNewAccionJustificacionToAttach.getClass(), accionJustificacionListNewAccionJustificacionToAttach.getAccionJustificacionPK());
                attachedAccionJustificacionListNew.add(accionJustificacionListNewAccionJustificacionToAttach);
            }
            accionJustificacionListNew = attachedAccionJustificacionListNew;
            analista.setAccionJustificacionList(accionJustificacionListNew);
            analista = em.merge(analista);
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setAnalista(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setAnalista(analista);
                usuariosNew = em.merge(usuariosNew);
            }
            for (Evento eventoListOldEvento : eventoListOld) {
                if (!eventoListNew.contains(eventoListOldEvento)) {
                    eventoListOldEvento.getAnalistaList().remove(analista);
                    eventoListOldEvento = em.merge(eventoListOldEvento);
                }
            }
            for (Evento eventoListNewEvento : eventoListNew) {
                if (!eventoListOld.contains(eventoListNewEvento)) {
                    eventoListNewEvento.getAnalistaList().add(analista);
                    eventoListNewEvento = em.merge(eventoListNewEvento);
                }
            }
            for (AccionConstancia accionConstanciaListNewAccionConstancia : accionConstanciaListNew) {
                if (!accionConstanciaListOld.contains(accionConstanciaListNewAccionConstancia)) {
                    Analista oldAnalistaOfAccionConstanciaListNewAccionConstancia = accionConstanciaListNewAccionConstancia.getAnalista();
                    accionConstanciaListNewAccionConstancia.setAnalista(analista);
                    accionConstanciaListNewAccionConstancia = em.merge(accionConstanciaListNewAccionConstancia);
                    if (oldAnalistaOfAccionConstanciaListNewAccionConstancia != null && !oldAnalistaOfAccionConstanciaListNewAccionConstancia.equals(analista)) {
                        oldAnalistaOfAccionConstanciaListNewAccionConstancia.getAccionConstanciaList().remove(accionConstanciaListNewAccionConstancia);
                        oldAnalistaOfAccionConstanciaListNewAccionConstancia = em.merge(oldAnalistaOfAccionConstanciaListNewAccionConstancia);
                    }
                }
            }
            for (AccionReclamo accionReclamoListNewAccionReclamo : accionReclamoListNew) {
                if (!accionReclamoListOld.contains(accionReclamoListNewAccionReclamo)) {
                    Analista oldAnalistaOfAccionReclamoListNewAccionReclamo = accionReclamoListNewAccionReclamo.getAnalista();
                    accionReclamoListNewAccionReclamo.setAnalista(analista);
                    accionReclamoListNewAccionReclamo = em.merge(accionReclamoListNewAccionReclamo);
                    if (oldAnalistaOfAccionReclamoListNewAccionReclamo != null && !oldAnalistaOfAccionReclamoListNewAccionReclamo.equals(analista)) {
                        oldAnalistaOfAccionReclamoListNewAccionReclamo.getAccionReclamoList().remove(accionReclamoListNewAccionReclamo);
                        oldAnalistaOfAccionReclamoListNewAccionReclamo = em.merge(oldAnalistaOfAccionReclamoListNewAccionReclamo);
                    }
                }
            }
            for (AccionJustificacion accionJustificacionListNewAccionJustificacion : accionJustificacionListNew) {
                if (!accionJustificacionListOld.contains(accionJustificacionListNewAccionJustificacion)) {
                    Analista oldAnalistaOfAccionJustificacionListNewAccionJustificacion = accionJustificacionListNewAccionJustificacion.getAnalista();
                    accionJustificacionListNewAccionJustificacion.setAnalista(analista);
                    accionJustificacionListNewAccionJustificacion = em.merge(accionJustificacionListNewAccionJustificacion);
                    if (oldAnalistaOfAccionJustificacionListNewAccionJustificacion != null && !oldAnalistaOfAccionJustificacionListNewAccionJustificacion.equals(analista)) {
                        oldAnalistaOfAccionJustificacionListNewAccionJustificacion.getAccionJustificacionList().remove(accionJustificacionListNewAccionJustificacion);
                        oldAnalistaOfAccionJustificacionListNewAccionJustificacion = em.merge(oldAnalistaOfAccionJustificacionListNewAccionJustificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = analista.getIdUsuario();
                if (findAnalista(id) == null) {
                    throw new NonexistentEntityException("The analista with id " + id + " no longer exists.");
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
            Analista analista;
            try {
                analista = em.getReference(Analista.class, id);
                analista.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The analista with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccionConstancia> accionConstanciaListOrphanCheck = analista.getAccionConstanciaList();
            for (AccionConstancia accionConstanciaListOrphanCheckAccionConstancia : accionConstanciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Analista (" + analista + ") cannot be destroyed since the AccionConstancia " + accionConstanciaListOrphanCheckAccionConstancia + " in its accionConstanciaList field has a non-nullable analista field.");
            }
            List<AccionReclamo> accionReclamoListOrphanCheck = analista.getAccionReclamoList();
            for (AccionReclamo accionReclamoListOrphanCheckAccionReclamo : accionReclamoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Analista (" + analista + ") cannot be destroyed since the AccionReclamo " + accionReclamoListOrphanCheckAccionReclamo + " in its accionReclamoList field has a non-nullable analista field.");
            }
            List<AccionJustificacion> accionJustificacionListOrphanCheck = analista.getAccionJustificacionList();
            for (AccionJustificacion accionJustificacionListOrphanCheckAccionJustificacion : accionJustificacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Analista (" + analista + ") cannot be destroyed since the AccionJustificacion " + accionJustificacionListOrphanCheckAccionJustificacion + " in its accionJustificacionList field has a non-nullable analista field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios usuarios = analista.getUsuarios();
            if (usuarios != null) {
                usuarios.setAnalista(null);
                usuarios = em.merge(usuarios);
            }
            List<Evento> eventoList = analista.getEventoList();
            for (Evento eventoListEvento : eventoList) {
                eventoListEvento.getAnalistaList().remove(analista);
                eventoListEvento = em.merge(eventoListEvento);
            }
            em.remove(analista);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Analista> findAnalistaEntities() {
        return findAnalistaEntities(true, -1, -1);
    }

    public List<Analista> findAnalistaEntities(int maxResults, int firstResult) {
        return findAnalistaEntities(false, maxResults, firstResult);
    }

    private List<Analista> findAnalistaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Analista.class));
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

    public Analista findAnalista(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Analista.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnalistaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Analista> rt = cq.from(Analista.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
