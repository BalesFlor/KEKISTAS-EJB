package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Roles;
import java.util.ArrayList;
import java.util.List;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RolesJpaController implements Serializable {

    public RolesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Roles roles) throws PreexistingEntityException, Exception {
        if (roles.getUsuariosList() == null) {
            roles.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : roles.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdUsuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            roles.setUsuariosList(attachedUsuariosList);
            em.persist(roles);
            for (Usuarios usuariosListUsuarios : roles.getUsuariosList()) {
                Roles oldIdRolOfUsuariosListUsuarios = usuariosListUsuarios.getRol();
                usuariosListUsuarios.setRol(roles);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdRolOfUsuariosListUsuarios != null) {
                    oldIdRolOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdRolOfUsuariosListUsuarios = em.merge(oldIdRolOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRoles(roles.getIdRol()) != null) {
                throw new PreexistingEntityException("Roles " + roles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Roles roles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles persistentRoles = em.find(Roles.class, roles.getIdRol());
            List<Usuarios> usuariosListOld = persistentRoles.getUsuariosList();
            List<Usuarios> usuariosListNew = roles.getUsuariosList();
            List<String> illegalOrphanMessages = null;
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios + " since its idRol field is not nullable.");
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
            roles.setUsuariosList(usuariosListNew);
            roles = em.merge(roles);
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Roles oldIdRolOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getRol();
                    usuariosListNewUsuarios.setRol(roles);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdRolOfUsuariosListNewUsuarios != null && !oldIdRolOfUsuariosListNewUsuarios.equals(roles)) {
                        oldIdRolOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdRolOfUsuariosListNewUsuarios = em.merge(oldIdRolOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = roles.getIdRol();
                if (findRoles(id) == null) {
                    throw new NonexistentEntityException("The roles with id " + id + " no longer exists.");
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
            Roles roles;
            try {
                roles = em.getReference(Roles.class, id);
                roles.getIdRol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usuarios> usuariosListOrphanCheck = roles.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Roles (" + roles + ") cannot be destroyed since the Usuarios " + usuariosListOrphanCheckUsuarios + " in its usuariosList field has a non-nullable idRol field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Roles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    public List<Roles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    private List<Roles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roles.class));
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

    public Roles findRoles(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roles.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Roles> rt = cq.from(Roles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
