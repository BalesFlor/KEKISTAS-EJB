package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.TipoConstancia;
import com.grsc.modelo.entities.AccionConstancia;
import com.grsc.modelo.entities.Constancia;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ConstanciaJpaController implements Serializable {

    public ConstanciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Constancia constancia) {
        if (constancia.getAccionConstanciaList() == null) {
            constancia.setAccionConstanciaList(new ArrayList<AccionConstancia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante idUsuario = constancia.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                constancia.setIdUsuario(idUsuario);
            }
            TipoConstancia idTipoConstancia = constancia.getIdTipoConstancia();
            if (idTipoConstancia != null) {
                idTipoConstancia = em.getReference(idTipoConstancia.getClass(), idTipoConstancia.getIdTipoConstancia());
                constancia.setIdTipoConstancia(idTipoConstancia);
            }
            List<AccionConstancia> attachedAccionConstanciaList = new ArrayList<AccionConstancia>();
            for (AccionConstancia accionConstanciaListAccionConstanciaToAttach : constancia.getAccionConstanciaList()) {
                accionConstanciaListAccionConstanciaToAttach = em.getReference(accionConstanciaListAccionConstanciaToAttach.getClass(), accionConstanciaListAccionConstanciaToAttach.getAccionConstanciaPK());
                attachedAccionConstanciaList.add(accionConstanciaListAccionConstanciaToAttach);
            }
            constancia.setAccionConstanciaList(attachedAccionConstanciaList);
            em.persist(constancia);
            if (idUsuario != null) {
                idUsuario.getConstanciaList().add(constancia);
                idUsuario = em.merge(idUsuario);
            }
            if (idTipoConstancia != null) {
                idTipoConstancia.getConstanciaList().add(constancia);
                idTipoConstancia = em.merge(idTipoConstancia);
            }
            for (AccionConstancia accionConstanciaListAccionConstancia : constancia.getAccionConstanciaList()) {
                Constancia oldConstanciaOfAccionConstanciaListAccionConstancia = accionConstanciaListAccionConstancia.getConstancia();
                accionConstanciaListAccionConstancia.setConstancia(constancia);
                accionConstanciaListAccionConstancia = em.merge(accionConstanciaListAccionConstancia);
                if (oldConstanciaOfAccionConstanciaListAccionConstancia != null) {
                    oldConstanciaOfAccionConstanciaListAccionConstancia.getAccionConstanciaList().remove(accionConstanciaListAccionConstancia);
                    oldConstanciaOfAccionConstanciaListAccionConstancia = em.merge(oldConstanciaOfAccionConstanciaListAccionConstancia);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Constancia constancia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Constancia persistentConstancia = em.find(Constancia.class, constancia.getIdConstancia());
            Estudiante idUsuarioOld = persistentConstancia.getIdUsuario();
            Estudiante idUsuarioNew = constancia.getIdUsuario();
            TipoConstancia idTipoConstanciaOld = persistentConstancia.getIdTipoConstancia();
            TipoConstancia idTipoConstanciaNew = constancia.getIdTipoConstancia();
            List<AccionConstancia> accionConstanciaListOld = persistentConstancia.getAccionConstanciaList();
            List<AccionConstancia> accionConstanciaListNew = constancia.getAccionConstanciaList();
            List<String> illegalOrphanMessages = null;
            for (AccionConstancia accionConstanciaListOldAccionConstancia : accionConstanciaListOld) {
                if (!accionConstanciaListNew.contains(accionConstanciaListOldAccionConstancia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccionConstancia " + accionConstanciaListOldAccionConstancia + " since its constancia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                constancia.setIdUsuario(idUsuarioNew);
            }
            if (idTipoConstanciaNew != null) {
                idTipoConstanciaNew = em.getReference(idTipoConstanciaNew.getClass(), idTipoConstanciaNew.getIdTipoConstancia());
                constancia.setIdTipoConstancia(idTipoConstanciaNew);
            }
            List<AccionConstancia> attachedAccionConstanciaListNew = new ArrayList<AccionConstancia>();
            for (AccionConstancia accionConstanciaListNewAccionConstanciaToAttach : accionConstanciaListNew) {
                accionConstanciaListNewAccionConstanciaToAttach = em.getReference(accionConstanciaListNewAccionConstanciaToAttach.getClass(), accionConstanciaListNewAccionConstanciaToAttach.getAccionConstanciaPK());
                attachedAccionConstanciaListNew.add(accionConstanciaListNewAccionConstanciaToAttach);
            }
            accionConstanciaListNew = attachedAccionConstanciaListNew;
            constancia.setAccionConstanciaList(accionConstanciaListNew);
            constancia = em.merge(constancia);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getConstanciaList().remove(constancia);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getConstanciaList().add(constancia);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            if (idTipoConstanciaOld != null && !idTipoConstanciaOld.equals(idTipoConstanciaNew)) {
                idTipoConstanciaOld.getConstanciaList().remove(constancia);
                idTipoConstanciaOld = em.merge(idTipoConstanciaOld);
            }
            if (idTipoConstanciaNew != null && !idTipoConstanciaNew.equals(idTipoConstanciaOld)) {
                idTipoConstanciaNew.getConstanciaList().add(constancia);
                idTipoConstanciaNew = em.merge(idTipoConstanciaNew);
            }
            for (AccionConstancia accionConstanciaListNewAccionConstancia : accionConstanciaListNew) {
                if (!accionConstanciaListOld.contains(accionConstanciaListNewAccionConstancia)) {
                    Constancia oldConstanciaOfAccionConstanciaListNewAccionConstancia = accionConstanciaListNewAccionConstancia.getConstancia();
                    accionConstanciaListNewAccionConstancia.setConstancia(constancia);
                    accionConstanciaListNewAccionConstancia = em.merge(accionConstanciaListNewAccionConstancia);
                    if (oldConstanciaOfAccionConstanciaListNewAccionConstancia != null && !oldConstanciaOfAccionConstanciaListNewAccionConstancia.equals(constancia)) {
                        oldConstanciaOfAccionConstanciaListNewAccionConstancia.getAccionConstanciaList().remove(accionConstanciaListNewAccionConstancia);
                        oldConstanciaOfAccionConstanciaListNewAccionConstancia = em.merge(oldConstanciaOfAccionConstanciaListNewAccionConstancia);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = constancia.getIdConstancia();
                if (findConstancia(id) == null) {
                    throw new NonexistentEntityException("The constancia with id " + id + " no longer exists.");
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
            Constancia constancia;
            try {
                constancia = em.getReference(Constancia.class, id);
                constancia.getIdConstancia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The constancia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccionConstancia> accionConstanciaListOrphanCheck = constancia.getAccionConstanciaList();
            for (AccionConstancia accionConstanciaListOrphanCheckAccionConstancia : accionConstanciaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Constancia (" + constancia + ") cannot be destroyed since the AccionConstancia " + accionConstanciaListOrphanCheckAccionConstancia + " in its accionConstanciaList field has a non-nullable constancia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudiante idUsuario = constancia.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getConstanciaList().remove(constancia);
                idUsuario = em.merge(idUsuario);
            }
            TipoConstancia idTipoConstancia = constancia.getIdTipoConstancia();
            if (idTipoConstancia != null) {
                idTipoConstancia.getConstanciaList().remove(constancia);
                idTipoConstancia = em.merge(idTipoConstancia);
            }
            em.remove(constancia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Constancia> findConstanciaEntities() {
        return findConstanciaEntities(true, -1, -1);
    }

    public List<Constancia> findConstanciaEntities(int maxResults, int firstResult) {
        return findConstanciaEntities(false, maxResults, firstResult);
    }

    private List<Constancia> findConstanciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Constancia.class));
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

    public Constancia findConstancia(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Constancia.class, id);
        } finally {
            em.close();
        }
    }

    public int getConstanciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Constancia> rt = cq.from(Constancia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
