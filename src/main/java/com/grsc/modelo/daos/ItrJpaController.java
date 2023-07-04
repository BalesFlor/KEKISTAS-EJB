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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itr persistentItr = em.find(Itr.class, itr.getIdItr());
            Departamento idDepartamentoOld = persistentItr.getIdDepartamento();
            Departamento idDepartamentoNew = itr.getIdDepartamento();
            List<Usuarios> usuariosListOld = persistentItr.getUsuariosList();
            List<Usuarios> usuariosListNew = itr.getUsuariosList();
            List<String> illegalOrphanMessages = null;
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios + " since its idItr field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getIdDepartamento());
                itr.setIdDepartamento(idDepartamentoNew);
            }
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            itr.setUsuariosList(usuariosListNew);
            itr = em.merge(itr);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getItrList().remove(itr);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getItrList().add(itr);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Itr oldIdItrOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getItr();
                    usuariosListNewUsuarios.setItr(itr);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdItrOfUsuariosListNewUsuarios != null && !oldIdItrOfUsuariosListNewUsuarios.equals(itr)) {
                        oldIdItrOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdItrOfUsuariosListNewUsuarios = em.merge(oldIdItrOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = itr.getIdItr();
                if (findItr(id) == null) {
                    throw new NonexistentEntityException("The itr with id " + id + " no longer exists.");
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
            Itr itr;
            try {
                itr = em.getReference(Itr.class, id);
                itr.getIdItr();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itr with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usuarios> usuariosListOrphanCheck = itr.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Itr (" + itr + ") cannot be destroyed since the Usuarios " + usuariosListOrphanCheckUsuarios + " in its usuariosList field has a non-nullable idItr field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento idDepartamento = itr.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getItrList().remove(itr);
                idDepartamento = em.merge(idDepartamento);
            }
            em.remove(itr);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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
                    
                    
                    itrRes = Itr.builder()
                            .idItr(idItr)
                            .nomItr(nomItr)
                            .idDepartamento(departamento)
                            .build();
                }
        }
        return itrRes;
        }finally {
            em.close();
        }        
    }
    public Itr findByIDItr(BigInteger idITR) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
