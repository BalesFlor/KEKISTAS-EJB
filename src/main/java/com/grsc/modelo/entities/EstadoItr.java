package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
@Entity
@Table(name = "ESTADO_ITR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoItr.findAll", query = "SELECT e FROM EstadoItr e"),
    @NamedQuery(name = "EstadoItr.findByIdEstado", query = "SELECT e FROM EstadoItr e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "EstadoItr.findByNomEstado", query = "SELECT e FROM EstadoItr e WHERE e.nomEstado = :nomEstado")})
public class EstadoItr implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTADO")
    private BigInteger idEstado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOM_ESTADO")
    private String nomEstado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstado")
    private Collection<Itr> itrCollection;

    public EstadoItr() {
    }

    public EstadoItr(BigInteger idEstado) {
        this.idEstado = idEstado;
    }

    public EstadoItr(BigInteger idEstado, String nomEstado) {
        this.idEstado = idEstado;
        this.nomEstado = nomEstado;
    }

    public BigInteger getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(BigInteger idEstado) {
        this.idEstado = idEstado;
    }

    public String getNomEstado() {
        return nomEstado;
    }

    public void setNomEstado(String nomEstado) {
        this.nomEstado = nomEstado;
    }

    @XmlTransient
    public Collection<Itr> getItrCollection() {
        return itrCollection;
    }

    public void setItrCollection(Collection<Itr> itrCollection) {
        this.itrCollection = itrCollection;
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
        if (!(object instanceof EstadoItr)) {
            return false;
        }
        EstadoItr other = (EstadoItr) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.EstadoItr[ idEstado=" + idEstado + " ]";
    }
    
}
