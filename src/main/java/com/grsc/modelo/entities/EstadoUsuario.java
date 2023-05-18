package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ESTADO_USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoUsuario.findAll", query = "SELECT e FROM EstadoUsuario e"),
    @NamedQuery(name = "EstadoUsuario.findByIdEstado", query = "SELECT e FROM EstadoUsuario e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "EstadoUsuario.findByEstadoUsuario", query = "SELECT e FROM EstadoUsuario e WHERE e.estadoUsuario = :estadoUsuario")})
public class EstadoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTADO")
    private BigInteger idEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ESTADO_USUARIO")
    private String estadoUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstadoUsuario")
    private List<Usuarios> usuariosList;

    public EstadoUsuario() {
    }

    public EstadoUsuario(BigInteger idEstado) {
        this.idEstado = idEstado;
    }

    public EstadoUsuario(BigInteger idEstado, String estadoUsuario) {
        this.idEstado = idEstado;
        this.estadoUsuario = estadoUsuario;
    }

    public BigInteger getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(BigInteger idEstado) {
        this.idEstado = idEstado;
    }

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    @XmlTransient
    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoUsuario)) {
            return false;
        }
        EstadoUsuario other = (EstadoUsuario) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.EstadoUsuario[ idEstado=" + idEstado + " ]";
    }
    
}
