package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Builder;

@Builder
@Entity
@Table(name = "ESTADO_PETICION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoPeticion.findAll", query = "SELECT e FROM EstadoPeticion e"),
    @NamedQuery(name = "EstadoPeticion.findByIdEstado", query = "SELECT e FROM EstadoPeticion e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "EstadoPeticion.findByNomEstado", query = "SELECT e FROM EstadoPeticion e WHERE e.nomEstado = :nomEstado")})
public class EstadoPeticion implements Serializable {

    private static final long serialVersionUID = 1L;
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

    public EstadoPeticion() {
    }

    public EstadoPeticion(BigInteger idEstado) {
        this.idEstado = idEstado;
    }

    public EstadoPeticion(BigInteger idEstado, String nomEstado) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoPeticion)) {
            return false;
        }
        EstadoPeticion other = (EstadoPeticion) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.EstadoPeticion[ idEstado=" + idEstado + " ]";
    }
    
}
