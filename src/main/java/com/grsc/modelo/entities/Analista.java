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
import javax.persistence.ManyToMany;
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
@Table(name = "ANALISTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Analista.findAll", query = "SELECT a FROM Analista a"),
    @NamedQuery(name = "Analista.findByIdUsuario", query = "SELECT a FROM Analista a WHERE a.idUsuario = :idUsuario")})
public class Analista implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    @ManyToMany(mappedBy = "analistaList")
    private List<Evento> eventoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analista")
    private List<AccionConstancia> accionConstanciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analista")
    private List<AccionReclamo> accionReclamoList;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
@OneToOne(cascade=CascadeType.ALL,optional = false)
    private Usuarios usuarios;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "analista")
    private List<AccionJustificacion> accionJustificacionList;

    public Analista() {
    }

    public Analista(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<Evento> getEventoList() {
        return eventoList;
    }

    public void setEventoList(List<Evento> eventoList) {
        this.eventoList = eventoList;
    }

    @XmlTransient
    public List<AccionConstancia> getAccionConstanciaList() {
        return accionConstanciaList;
    }

    public void setAccionConstanciaList(List<AccionConstancia> accionConstanciaList) {
        this.accionConstanciaList = accionConstanciaList;
    }

    @XmlTransient
    public List<AccionReclamo> getAccionReclamoList() {
        return accionReclamoList;
    }

    public void setAccionReclamoList(List<AccionReclamo> accionReclamoList) {
        this.accionReclamoList = accionReclamoList;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @XmlTransient
    public List<AccionJustificacion> getAccionJustificacionList() {
        return accionJustificacionList;
    }

    public void setAccionJustificacionList(List<AccionJustificacion> accionJustificacionList) {
        this.accionJustificacionList = accionJustificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Analista)) {
            return false;
        }
        Analista other = (Analista) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Analista[ idUsuario=" + idUsuario + " ]";
    }
    
}
