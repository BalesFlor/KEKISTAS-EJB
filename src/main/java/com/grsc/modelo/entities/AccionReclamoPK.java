package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AccionReclamoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RECLAMO")
    private BigInteger idReclamo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;

    public AccionReclamoPK() {
    }

    public AccionReclamoPK(BigInteger idReclamo, BigInteger idUsuario) {
        this.idReclamo = idReclamo;
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(BigInteger idReclamo) {
        this.idReclamo = idReclamo;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReclamo != null ? idReclamo.hashCode() : 0);
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccionReclamoPK)) {
            return false;
        }
        AccionReclamoPK other = (AccionReclamoPK) object;
        if ((this.idReclamo == null && other.idReclamo != null) || (this.idReclamo != null && !this.idReclamo.equals(other.idReclamo))) {
            return false;
        }
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionReclamoPK[ idReclamo=" + idReclamo + ", idUsuario=" + idUsuario + " ]";
    }
    
}
