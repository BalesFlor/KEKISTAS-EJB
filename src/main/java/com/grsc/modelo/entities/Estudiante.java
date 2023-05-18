package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "ESTUDIANTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByIdUsuario", query = "SELECT e FROM Estudiante e WHERE e.idUsuario = :idUsuario")})
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<ConvocatoriaAsistencia> convocatoriaAsistenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<Constancia> constanciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<Reclamo> reclamoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<Justificacion> justificacionList;
    
    @JoinColumn(name = "ANIO_GEN", referencedColumnName = "ANIO")
    @ManyToOne
    private Generacion anioGen;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @OneToOne(cascade=CascadeType.ALL,optional = false)
    private Usuarios usuarios;

    public Estudiante() {
    }

    public Estudiante(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<ConvocatoriaAsistencia> getConvocatoriaAsistenciaList() {
        return convocatoriaAsistenciaList;
    }

    public void setConvocatoriaAsistenciaList(List<ConvocatoriaAsistencia> convocatoriaAsistenciaList) {
        this.convocatoriaAsistenciaList = convocatoriaAsistenciaList;
    }

    @XmlTransient
    public List<Constancia> getConstanciaList() {
        return constanciaList;
    }

    public void setConstanciaList(List<Constancia> constanciaList) {
        this.constanciaList = constanciaList;
    }

    @XmlTransient
    public List<Reclamo> getReclamoList() {
        return reclamoList;
    }

    public void setReclamoList(List<Reclamo> reclamoList) {
        this.reclamoList = reclamoList;
    }

    @XmlTransient
    public List<Justificacion> getJustificacionList() {
        return justificacionList;
    }

    public void setJustificacionList(List<Justificacion> justificacionList) {
        this.justificacionList = justificacionList;
    }

    public Generacion getAnioGen() {
        return anioGen;
    }

    public void setAnioGen(Generacion anioGen) {
        this.anioGen = anioGen;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Estudiante[ idUsuario=" + idUsuario + " ]";
    }
    
}
