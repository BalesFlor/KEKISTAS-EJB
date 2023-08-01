package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.EstadoUsuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EstadoUsuarioJpaController implements Serializable {

    public EstadoUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoUsuario estadoUsuario) throws PreexistingEntityException, Exception {
        if (estadoUsuario.getUsuariosList() == null) {
            estadoUsuario.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : estadoUsuario.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdUsuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            estadoUsuario.setUsuariosList(attachedUsuariosList);
            em.persist(estadoUsuario);
            for (Usuarios usuariosListUsuarios : estadoUsuario.getUsuariosList()) {
                EstadoUsuario oldIdEstadoUsuarioOfUsuariosListUsuarios = usuariosListUsuarios.getIdEstadoUsuario();
                usuariosListUsuarios.setIdEstadoUsuario(estadoUsuario);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdEstadoUsuarioOfUsuariosListUsuarios != null) {
                    oldIdEstadoUsuarioOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdEstadoUsuarioOfUsuariosListUsuarios = em.merge(oldIdEstadoUsuarioOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstadoUsuario(estadoUsuario.getIdEstado()) != null) {
                throw new PreexistingEntityException("EstadoUsuario " + estadoUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoUsuario estadoUsuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstadoUsuario persistentEstadoUsuario = em.find(EstadoUsuario.class, estadoUsuario.getIdEstado());
            List<Usuarios> usuariosListOld = persistentEstadoUsuario.getUsuariosList();
            List<Usuarios> usuariosListNew = estadoUsuario.getUsuariosList();
            List<String> illegalOrphanMessages = null;
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios + " since its idEstadoUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            estadoUsuario.setUsuariosList(usuariosListNew);
            estadoUsuario = em.merge(estadoUsuario);
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    EstadoUsuario oldIdEstadoUsuarioOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getIdEstadoUsuario();
                    usuariosListNewUsuarios.setIdEstadoUsuario(estadoUsuario);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdEstadoUsuarioOfUsuariosListNewUsuarios != null && !oldIdEstadoUsuarioOfUsuariosListNewUsuarios.equals(estadoUsuario)) {
                        oldIdEstadoUsuarioOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdEstadoUsuarioOfUsuariosListNewUsuarios = em.merge(oldIdEstadoUsuarioOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = estadoUsuario.getIdEstado();
                if (findEstadoUsuario(id) == null) {
                    throw new NonexistentEntityException("The estadoUsuario with id " + id + " no longer exists.");
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
            EstadoUsuario estadoUsuario;
            try {
                estadoUsuario = em.getReference(EstadoUsuario.class, id);
                estadoUsuario.getIdEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoUsuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usuarios> usuariosListOrphanCheck = estadoUsuario.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoUsuario (" + estadoUsuario + ") cannot be destroyed since the Usuarios " + usuariosListOrphanCheckUsuarios + " in its usuariosList field has a non-nullable idEstadoUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EstadoUsuario> findEstadoUsuarioEntities() {
        return findEstadoUsuarioEntities(true, -1, -1);
    }

    public List<EstadoUsuario> findEstadoUsuarioEntities(int maxResults, int firstResult) {
        return findEstadoUsuarioEntities(false, maxResults, firstResult);
    }

    private List<EstadoUsuario> findEstadoUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoUsuario.class));
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

    public EstadoUsuario findEstadoUsuario(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoUsuario> rt = cq.from(EstadoUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public EstadoUsuario findEstadoUsuario(String nombre) {
        EntityManager em = getEntityManager();
        EstadoUsuario estadoRes = new EstadoUsuario();
        try {
            List<EstadoUsuario> listaResultado = em.createNamedQuery("EstadoUsuario.findByEstadoUsuario")
                    .setParameter("estadoUsuario", nombre)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {
                    
                    BigInteger id = listaResultado.get(i).getIdEstado();
                    String nom = listaResultado.get(i).getEstadoUsuario();

                    estadoRes = EstadoUsuario.builder()
                            .idEstado(id)
                            .estadoUsuario(nom)
                            .build();
                }
            }
            return estadoRes;
        } finally {
            em.close();
        }
    }

}
