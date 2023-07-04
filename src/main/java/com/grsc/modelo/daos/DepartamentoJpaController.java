package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import com.grsc.modelo.entities.Departamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Localidad;
import java.util.ArrayList;
import java.util.List;
import com.grsc.modelo.entities.Itr;
import com.grsc.modelo.entities.Usuarios;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) throws PreexistingEntityException, Exception {
        if (departamento.getLocalidadList() == null) {
            departamento.setLocalidadList(new ArrayList<Localidad>());
        }
        if (departamento.getItrList() == null) {
            departamento.setItrList(new ArrayList<Itr>());
        }
        if (departamento.getUsuariosList() == null) {
            departamento.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Localidad> attachedLocalidadList = new ArrayList<Localidad>();
            for (Localidad localidadListLocalidadToAttach : departamento.getLocalidadList()) {
                localidadListLocalidadToAttach = em.getReference(localidadListLocalidadToAttach.getClass(), localidadListLocalidadToAttach.getIdLocalidad());
                attachedLocalidadList.add(localidadListLocalidadToAttach);
            }
            departamento.setLocalidadList(attachedLocalidadList);
            List<Itr> attachedItrList = new ArrayList<Itr>();
            for (Itr itrListItrToAttach : departamento.getItrList()) {
                itrListItrToAttach = em.getReference(itrListItrToAttach.getClass(), itrListItrToAttach.getIdItr());
                attachedItrList.add(itrListItrToAttach);
            }
            departamento.setItrList(attachedItrList);
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : departamento.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(), usuariosListUsuariosToAttach.getIdUsuario());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            departamento.setUsuariosList(attachedUsuariosList);
            em.persist(departamento);
            for (Localidad localidadListLocalidad : departamento.getLocalidadList()) {
                Departamento oldIdDepartamentoOfLocalidadListLocalidad = localidadListLocalidad.getIdDepartamento();
                localidadListLocalidad.setIdDepartamento(departamento);
                localidadListLocalidad = em.merge(localidadListLocalidad);
                if (oldIdDepartamentoOfLocalidadListLocalidad != null) {
                    oldIdDepartamentoOfLocalidadListLocalidad.getLocalidadList().remove(localidadListLocalidad);
                    oldIdDepartamentoOfLocalidadListLocalidad = em.merge(oldIdDepartamentoOfLocalidadListLocalidad);
                }
            }
            for (Itr itrListItr : departamento.getItrList()) {
                Departamento oldIdDepartamentoOfItrListItr = itrListItr.getIdDepartamento();
                itrListItr.setIdDepartamento(departamento);
                itrListItr = em.merge(itrListItr);
                if (oldIdDepartamentoOfItrListItr != null) {
                    oldIdDepartamentoOfItrListItr.getItrList().remove(itrListItr);
                    oldIdDepartamentoOfItrListItr = em.merge(oldIdDepartamentoOfItrListItr);
                }
            }
            for (Usuarios usuariosListUsuarios : departamento.getUsuariosList()) {
                Departamento oldIdDepartamentoOfUsuariosListUsuarios = usuariosListUsuarios.getDepartamento();
                usuariosListUsuarios.setDepartamento(departamento);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldIdDepartamentoOfUsuariosListUsuarios != null) {
                    oldIdDepartamentoOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldIdDepartamentoOfUsuariosListUsuarios = em.merge(oldIdDepartamentoOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDepartamento(departamento.getIdDepartamento()) != null) {
                throw new PreexistingEntityException("Departamento " + departamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepartamento());
            List<Localidad> localidadListOld = persistentDepartamento.getLocalidadList();
            List<Localidad> localidadListNew = departamento.getLocalidadList();
            List<Itr> itrListOld = persistentDepartamento.getItrList();
            List<Itr> itrListNew = departamento.getItrList();
            List<Usuarios> usuariosListOld = persistentDepartamento.getUsuariosList();
            List<Usuarios> usuariosListNew = departamento.getUsuariosList();
            List<String> illegalOrphanMessages = null;
            for (Localidad localidadListOldLocalidad : localidadListOld) {
                if (!localidadListNew.contains(localidadListOldLocalidad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Localidad " + localidadListOldLocalidad + " since its idDepartamento field is not nullable.");
                }
            }
            for (Itr itrListOldItr : itrListOld) {
                if (!itrListNew.contains(itrListOldItr)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Itr " + itrListOldItr + " since its idDepartamento field is not nullable.");
                }
            }
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios + " since its idDepartamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Localidad> attachedLocalidadListNew = new ArrayList<Localidad>();
            for (Localidad localidadListNewLocalidadToAttach : localidadListNew) {
                localidadListNewLocalidadToAttach = em.getReference(localidadListNewLocalidadToAttach.getClass(), localidadListNewLocalidadToAttach.getIdLocalidad());
                attachedLocalidadListNew.add(localidadListNewLocalidadToAttach);
            }
            localidadListNew = attachedLocalidadListNew;
            departamento.setLocalidadList(localidadListNew);
            List<Itr> attachedItrListNew = new ArrayList<Itr>();
            for (Itr itrListNewItrToAttach : itrListNew) {
                itrListNewItrToAttach = em.getReference(itrListNewItrToAttach.getClass(), itrListNewItrToAttach.getIdItr());
                attachedItrListNew.add(itrListNewItrToAttach);
            }
            itrListNew = attachedItrListNew;
            departamento.setItrList(itrListNew);
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(), usuariosListNewUsuariosToAttach.getIdUsuario());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            departamento.setUsuariosList(usuariosListNew);
            departamento = em.merge(departamento);
            for (Localidad localidadListNewLocalidad : localidadListNew) {
                if (!localidadListOld.contains(localidadListNewLocalidad)) {
                    Departamento oldIdDepartamentoOfLocalidadListNewLocalidad = localidadListNewLocalidad.getIdDepartamento();
                    localidadListNewLocalidad.setIdDepartamento(departamento);
                    localidadListNewLocalidad = em.merge(localidadListNewLocalidad);
                    if (oldIdDepartamentoOfLocalidadListNewLocalidad != null && !oldIdDepartamentoOfLocalidadListNewLocalidad.equals(departamento)) {
                        oldIdDepartamentoOfLocalidadListNewLocalidad.getLocalidadList().remove(localidadListNewLocalidad);
                        oldIdDepartamentoOfLocalidadListNewLocalidad = em.merge(oldIdDepartamentoOfLocalidadListNewLocalidad);
                    }
                }
            }
            for (Itr itrListNewItr : itrListNew) {
                if (!itrListOld.contains(itrListNewItr)) {
                    Departamento oldIdDepartamentoOfItrListNewItr = itrListNewItr.getIdDepartamento();
                    itrListNewItr.setIdDepartamento(departamento);
                    itrListNewItr = em.merge(itrListNewItr);
                    if (oldIdDepartamentoOfItrListNewItr != null && !oldIdDepartamentoOfItrListNewItr.equals(departamento)) {
                        oldIdDepartamentoOfItrListNewItr.getItrList().remove(itrListNewItr);
                        oldIdDepartamentoOfItrListNewItr = em.merge(oldIdDepartamentoOfItrListNewItr);
                    }
                }
            }
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Departamento oldIdDepartamentoOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getDepartamento();
                    usuariosListNewUsuarios.setDepartamento(departamento);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldIdDepartamentoOfUsuariosListNewUsuarios != null && !oldIdDepartamentoOfUsuariosListNewUsuarios.equals(departamento)) {
                        oldIdDepartamentoOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldIdDepartamentoOfUsuariosListNewUsuarios = em.merge(oldIdDepartamentoOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = departamento.getIdDepartamento();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepartamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Localidad> localidadListOrphanCheck = departamento.getLocalidadList();
            for (Localidad localidadListOrphanCheckLocalidad : localidadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Localidad " + localidadListOrphanCheckLocalidad + " in its localidadList field has a non-nullable idDepartamento field.");
            }
            List<Itr> itrListOrphanCheck = departamento.getItrList();
            for (Itr itrListOrphanCheckItr : itrListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Itr " + itrListOrphanCheckItr + " in its itrList field has a non-nullable idDepartamento field.");
            }
            List<Usuarios> usuariosListOrphanCheck = departamento.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Usuarios " + usuariosListOrphanCheckUsuarios + " in its usuariosList field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public Departamento findDepto(String nombre) {
        EntityManager em = getEntityManager();
        Departamento deptoRes = new Departamento();
        try{
        List<Departamento> listaResultado = em.createNamedQuery("Departamento.findByNomDepartamento")
                    .setParameter("nomDepartamento", nombre)
                .getResultList();
        if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    BigInteger idDepto = listaResultado.get(i).getIdDepartamento();
                    String nombreDepto = listaResultado.get(i).getNomDepartamento();


                    deptoRes = Departamento.builder()
                           .idDepartamento(idDepto)
                            .nomDepartamento(nombreDepto)
                            .build();
                }
        }
        return deptoRes;
        }finally {
            em.close();
        }
    } 
    
}
