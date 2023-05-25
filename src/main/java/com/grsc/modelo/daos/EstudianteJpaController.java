package com.grsc.modelo.daos;

import com.grsc.exceptions.IllegalOrphanException;
import com.grsc.exceptions.NonexistentEntityException;
import com.grsc.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.grsc.modelo.entities.Generacion;
import com.grsc.modelo.entities.Usuarios;
import com.grsc.modelo.entities.ConvocatoriaAsistencia;
import java.util.ArrayList;
import java.util.List;
import com.grsc.modelo.entities.Constancia;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Reclamo;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import javax.transaction.Transactional;

public class EstudianteJpaController implements Serializable {

    public EstudianteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiante estudiante) throws IllegalOrphanException, PreexistingEntityException, Exception {
       EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            em.merge(estudiante);
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudiante estudiante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante persistentEstudiante = em.find(Estudiante.class, estudiante.getIdUsuario());
            Generacion anioGenOld = persistentEstudiante.getAnioGen();
            Generacion anioGenNew = estudiante.getAnioGen();
            Usuarios usuariosOld = persistentEstudiante.getUsuarios();
            Usuarios usuariosNew = estudiante.getUsuarios();
            List<ConvocatoriaAsistencia> convocatoriaAsistenciaListOld = persistentEstudiante.getConvocatoriaAsistenciaList();
            List<ConvocatoriaAsistencia> convocatoriaAsistenciaListNew = estudiante.getConvocatoriaAsistenciaList();
            List<Constancia> constanciaListOld = persistentEstudiante.getConstanciaList();
            List<Constancia> constanciaListNew = estudiante.getConstanciaList();
            List<Reclamo> reclamoListOld = persistentEstudiante.getReclamoList();
            List<Reclamo> reclamoListNew = estudiante.getReclamoList();
            List<Justificacion> justificacionListOld = persistentEstudiante.getJustificacionList();
            List<Justificacion> justificacionListNew = estudiante.getJustificacionList();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Estudiante oldEstudianteOfUsuarios = usuariosNew.getEstudiante();
                if (oldEstudianteOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Estudiante whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListOldConvocatoriaAsistencia : convocatoriaAsistenciaListOld) {
                if (!convocatoriaAsistenciaListNew.contains(convocatoriaAsistenciaListOldConvocatoriaAsistencia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConvocatoriaAsistencia " + convocatoriaAsistenciaListOldConvocatoriaAsistencia + " since its estudiante field is not nullable.");
                }
            }
            for (Constancia constanciaListOldConstancia : constanciaListOld) {
                if (!constanciaListNew.contains(constanciaListOldConstancia)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Constancia " + constanciaListOldConstancia + " since its idUsuario field is not nullable.");
                }
            }
            for (Reclamo reclamoListOldReclamo : reclamoListOld) {
                if (!reclamoListNew.contains(reclamoListOldReclamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reclamo " + reclamoListOldReclamo + " since its idUsuario field is not nullable.");
                }
            }
            for (Justificacion justificacionListOldJustificacion : justificacionListOld) {
                if (!justificacionListNew.contains(justificacionListOldJustificacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Justificacion " + justificacionListOldJustificacion + " since its idUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (anioGenNew != null) {
                anioGenNew = em.getReference(anioGenNew.getClass(), anioGenNew.getAnio());
                estudiante.setAnioGen(anioGenNew);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getIdUsuario());
                estudiante.setUsuarios(usuariosNew);
            }
            List<ConvocatoriaAsistencia> attachedConvocatoriaAsistenciaListNew = new ArrayList<ConvocatoriaAsistencia>();
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach : convocatoriaAsistenciaListNew) {
                convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach = em.getReference(convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach.getClass(), convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach.getConvocatoriaAsistenciaPK());
                attachedConvocatoriaAsistenciaListNew.add(convocatoriaAsistenciaListNewConvocatoriaAsistenciaToAttach);
            }
            convocatoriaAsistenciaListNew = attachedConvocatoriaAsistenciaListNew;
            estudiante.setConvocatoriaAsistenciaList(convocatoriaAsistenciaListNew);
            List<Constancia> attachedConstanciaListNew = new ArrayList<Constancia>();
            for (Constancia constanciaListNewConstanciaToAttach : constanciaListNew) {
                constanciaListNewConstanciaToAttach = em.getReference(constanciaListNewConstanciaToAttach.getClass(), constanciaListNewConstanciaToAttach.getIdConstancia());
                attachedConstanciaListNew.add(constanciaListNewConstanciaToAttach);
            }
            constanciaListNew = attachedConstanciaListNew;
            estudiante.setConstanciaList(constanciaListNew);
            List<Reclamo> attachedReclamoListNew = new ArrayList<Reclamo>();
            for (Reclamo reclamoListNewReclamoToAttach : reclamoListNew) {
                reclamoListNewReclamoToAttach = em.getReference(reclamoListNewReclamoToAttach.getClass(), reclamoListNewReclamoToAttach.getIdReclamo());
                attachedReclamoListNew.add(reclamoListNewReclamoToAttach);
            }
            reclamoListNew = attachedReclamoListNew;
            estudiante.setReclamoList(reclamoListNew);
            List<Justificacion> attachedJustificacionListNew = new ArrayList<Justificacion>();
            for (Justificacion justificacionListNewJustificacionToAttach : justificacionListNew) {
                justificacionListNewJustificacionToAttach = em.getReference(justificacionListNewJustificacionToAttach.getClass(), justificacionListNewJustificacionToAttach.getIdJustificacion());
                attachedJustificacionListNew.add(justificacionListNewJustificacionToAttach);
            }
            justificacionListNew = attachedJustificacionListNew;
            estudiante.setJustificacionList(justificacionListNew);
            estudiante = em.merge(estudiante);
            if (anioGenOld != null && !anioGenOld.equals(anioGenNew)) {
                anioGenOld.getEstudianteList().remove(estudiante);
                anioGenOld = em.merge(anioGenOld);
            }
            if (anioGenNew != null && !anioGenNew.equals(anioGenOld)) {
                anioGenNew.getEstudianteList().add(estudiante);
                anioGenNew = em.merge(anioGenNew);
            }
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setEstudiante(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setEstudiante(estudiante);
                usuariosNew = em.merge(usuariosNew);
            }
            for (ConvocatoriaAsistencia convocatoriaAsistenciaListNewConvocatoriaAsistencia : convocatoriaAsistenciaListNew) {
                if (!convocatoriaAsistenciaListOld.contains(convocatoriaAsistenciaListNewConvocatoriaAsistencia)) {
                    Estudiante oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia = convocatoriaAsistenciaListNewConvocatoriaAsistencia.getEstudiante();
                    convocatoriaAsistenciaListNewConvocatoriaAsistencia.setEstudiante(estudiante);
                    convocatoriaAsistenciaListNewConvocatoriaAsistencia = em.merge(convocatoriaAsistenciaListNewConvocatoriaAsistencia);
                    if (oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia != null && !oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia.equals(estudiante)) {
                        oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia.getConvocatoriaAsistenciaList().remove(convocatoriaAsistenciaListNewConvocatoriaAsistencia);
                        oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia = em.merge(oldEstudianteOfConvocatoriaAsistenciaListNewConvocatoriaAsistencia);
                    }
                }
            }
            for (Constancia constanciaListNewConstancia : constanciaListNew) {
                if (!constanciaListOld.contains(constanciaListNewConstancia)) {
                    Estudiante oldIdUsuarioOfConstanciaListNewConstancia = constanciaListNewConstancia.getIdUsuario();
                    constanciaListNewConstancia.setIdUsuario(estudiante);
                    constanciaListNewConstancia = em.merge(constanciaListNewConstancia);
                    if (oldIdUsuarioOfConstanciaListNewConstancia != null && !oldIdUsuarioOfConstanciaListNewConstancia.equals(estudiante)) {
                        oldIdUsuarioOfConstanciaListNewConstancia.getConstanciaList().remove(constanciaListNewConstancia);
                        oldIdUsuarioOfConstanciaListNewConstancia = em.merge(oldIdUsuarioOfConstanciaListNewConstancia);
                    }
                }
            }
            for (Reclamo reclamoListNewReclamo : reclamoListNew) {
                if (!reclamoListOld.contains(reclamoListNewReclamo)) {
                    Estudiante oldIdUsuarioOfReclamoListNewReclamo = reclamoListNewReclamo.getIdUsuario();
                    reclamoListNewReclamo.setIdUsuario(estudiante);
                    reclamoListNewReclamo = em.merge(reclamoListNewReclamo);
                    if (oldIdUsuarioOfReclamoListNewReclamo != null && !oldIdUsuarioOfReclamoListNewReclamo.equals(estudiante)) {
                        oldIdUsuarioOfReclamoListNewReclamo.getReclamoList().remove(reclamoListNewReclamo);
                        oldIdUsuarioOfReclamoListNewReclamo = em.merge(oldIdUsuarioOfReclamoListNewReclamo);
                    }
                }
            }
            for (Justificacion justificacionListNewJustificacion : justificacionListNew) {
                if (!justificacionListOld.contains(justificacionListNewJustificacion)) {
                    Estudiante oldIdUsuarioOfJustificacionListNewJustificacion = justificacionListNewJustificacion.getIdUsuario();
                    justificacionListNewJustificacion.setIdUsuario(estudiante);
                    justificacionListNewJustificacion = em.merge(justificacionListNewJustificacion);
                    if (oldIdUsuarioOfJustificacionListNewJustificacion != null && !oldIdUsuarioOfJustificacionListNewJustificacion.equals(estudiante)) {
                        oldIdUsuarioOfJustificacionListNewJustificacion.getJustificacionList().remove(justificacionListNewJustificacion);
                        oldIdUsuarioOfJustificacionListNewJustificacion = em.merge(oldIdUsuarioOfJustificacionListNewJustificacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = estudiante.getIdUsuario();
                if (findEstudiante(id) == null) {
                    throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Transactional
    public void destroy(BigInteger id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante;
            try {
                estudiante = em.getReference(Estudiante.class, id);
                estudiante.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.", enfe);
            }
            
            List<ConvocatoriaAsistencia> listaConvocatoriaAsistencias = estudiante.getConvocatoriaAsistenciaList();
            if (listaConvocatoriaAsistencias != null) {
                estudiante.setConvocatoriaAsistenciaList(null);
                listaConvocatoriaAsistencias = em.merge(listaConvocatoriaAsistencias);
            }
            
            List<Constancia> listaConstancias = estudiante.getConstanciaList();
            if (listaConstancias != null) {
                estudiante.setConstanciaList(null);
                listaConstancias = em.merge(listaConstancias);
            }
            
            List<Reclamo> listaReclamos = estudiante.getReclamoList();
            if (listaReclamos != null) {
                estudiante.setReclamoList(null);
                listaReclamos = em.merge(listaReclamos);
            }
            List<Justificacion> listaJustificacions = estudiante.getJustificacionList();
            if (listaJustificacions != null) {
                estudiante.setJustificacionList(null);
                listaJustificacions = em.merge(listaJustificacions);
            }
            Generacion anioGen = estudiante.getAnioGen();
            if (anioGen != null) {
                anioGen.getEstudianteList().remove(estudiante);
                em.merge(anioGen);
            }
            Usuarios usuarios = estudiante.getUsuarios();
            if (usuarios != null) {
                usuarios.setEstudiante(null);
                em.merge(usuarios);
                
                }
            
            em.remove(estudiante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudiante> findEstudianteEntities() {
        return findEstudianteEntities(true, -1, -1);
    }

    public List<Estudiante> findEstudianteEntities(int maxResults, int firstResult) {
        return findEstudianteEntities(false, maxResults, firstResult);
    }

    private List<Estudiante> findEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiante.class));
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

    public Estudiante findEstudiante(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiante> rt = cq.from(Estudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
