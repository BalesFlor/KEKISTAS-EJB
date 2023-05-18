package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Generacion;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GeneracionJpaController implements Serializable {

    public GeneracionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Generacion generacion) throws PreexistingEntityException, Exception {
        if (generacion.getEstudianteList() == null) {
            generacion.setEstudianteList(new ArrayList<Estudiante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudiante> attachedEstudianteList = new ArrayList<Estudiante>();
            for (Estudiante estudianteListEstudianteToAttach : generacion.getEstudianteList()) {
                estudianteListEstudianteToAttach = em.getReference(estudianteListEstudianteToAttach.getClass(), estudianteListEstudianteToAttach.getIdUsuario());
                attachedEstudianteList.add(estudianteListEstudianteToAttach);
            }
            generacion.setEstudianteList(attachedEstudianteList);
            em.persist(generacion);
            for (Estudiante estudianteListEstudiante : generacion.getEstudianteList()) {
                Generacion oldAnioGenOfEstudianteListEstudiante = estudianteListEstudiante.getAnioGen();
                estudianteListEstudiante.setAnioGen(generacion);
                estudianteListEstudiante = em.merge(estudianteListEstudiante);
                if (oldAnioGenOfEstudianteListEstudiante != null) {
                    oldAnioGenOfEstudianteListEstudiante.getEstudianteList().remove(estudianteListEstudiante);
                    oldAnioGenOfEstudianteListEstudiante = em.merge(oldAnioGenOfEstudianteListEstudiante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGeneracion(generacion.getAnio()) != null) {
                throw new PreexistingEntityException("Generacion " + generacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Generacion generacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Generacion persistentGeneracion = em.find(Generacion.class, generacion.getAnio());
            List<Estudiante> estudianteListOld = persistentGeneracion.getEstudianteList();
            List<Estudiante> estudianteListNew = generacion.getEstudianteList();
            List<String> illegalOrphanMessages = null;
            for (Estudiante estudianteListOldEstudiante : estudianteListOld) {
                if (!estudianteListNew.contains(estudianteListOldEstudiante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudiante " + estudianteListOldEstudiante + " since its anioGen field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Estudiante> attachedEstudianteListNew = new ArrayList<Estudiante>();
            for (Estudiante estudianteListNewEstudianteToAttach : estudianteListNew) {
                estudianteListNewEstudianteToAttach = em.getReference(estudianteListNewEstudianteToAttach.getClass(), estudianteListNewEstudianteToAttach.getIdUsuario());
                attachedEstudianteListNew.add(estudianteListNewEstudianteToAttach);
            }
            estudianteListNew = attachedEstudianteListNew;
            generacion.setEstudianteList(estudianteListNew);
            generacion = em.merge(generacion);
            for (Estudiante estudianteListNewEstudiante : estudianteListNew) {
                if (!estudianteListOld.contains(estudianteListNewEstudiante)) {
                    Generacion oldAnioGenOfEstudianteListNewEstudiante = estudianteListNewEstudiante.getAnioGen();
                    estudianteListNewEstudiante.setAnioGen(generacion);
                    estudianteListNewEstudiante = em.merge(estudianteListNewEstudiante);
                    if (oldAnioGenOfEstudianteListNewEstudiante != null && !oldAnioGenOfEstudianteListNewEstudiante.equals(generacion)) {
                        oldAnioGenOfEstudianteListNewEstudiante.getEstudianteList().remove(estudianteListNewEstudiante);
                        oldAnioGenOfEstudianteListNewEstudiante = em.merge(oldAnioGenOfEstudianteListNewEstudiante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = generacion.getAnio();
                if (findGeneracion(id) == null) {
                    throw new NonexistentEntityException("The generacion with id " + id + " no longer exists.");
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
            Generacion generacion;
            try {
                generacion = em.getReference(Generacion.class, id);
                generacion.getAnio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The generacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Estudiante> estudianteListOrphanCheck = generacion.getEstudianteList();
            for (Estudiante estudianteListOrphanCheckEstudiante : estudianteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Generacion (" + generacion + ") cannot be destroyed since the Estudiante " + estudianteListOrphanCheckEstudiante + " in its estudianteList field has a non-nullable anioGen field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(generacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Generacion> findGeneracionEntities() {
        return findGeneracionEntities(true, -1, -1);
    }

    public List<Generacion> findGeneracionEntities(int maxResults, int firstResult) {
        return findGeneracionEntities(false, maxResults, firstResult);
    }

    private List<Generacion> findGeneracionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Generacion.class));
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

    public Generacion findGeneracion(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Generacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneracionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Generacion> rt = cq.from(Generacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Generacion findByAnio(BigInteger anio){
        EntityManager em = getEntityManager();
        Generacion gen = null;
        try{
            List<Generacion> listaResultado = em.createNamedQuery("Generacion.findByAnio")
                    .setParameter("anio", anio)
                    .getResultList();
            if (!listaResultado.isEmpty()) {
                for (int i = 0; i < listaResultado.size(); i++) {

                    String nomGeneracion= listaResultado.get(i).getNomGeneracion();
                    
                    gen = new Generacion(nomGeneracion, anio);

                }
            }
                                
             return gen;
        }finally {
            em.close();
        }
       
    }
}
