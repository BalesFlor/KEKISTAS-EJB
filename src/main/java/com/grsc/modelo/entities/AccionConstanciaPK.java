package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AccionConstanciaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONSTANCIA")
    private BigInteger idConstancia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;

    public AccionConstanciaPK() {
    }

    public AccionConstanciaPK(BigInteger idConstancia, BigInteger idUsuario) {
        this.idConstancia = idConstancia;
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdConstancia() {
        return idConstancia;
    }

    public void setIdConstancia(BigInteger idConstancia) {
        this.idConstancia = idConstancia;
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
        hash += (idConstancia != null ? idConstancia.hashCode() : 0);
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccionConstanciaPK)) {
            return false;
        }
        AccionConstanciaPK other = (AccionConstanciaPK) object;
        if ((this.idConstancia == null && other.idConstancia != null) || (this.idConstancia != null && !this.idConstancia.equals(other.idConstancia))) {
            return false;
        }
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionConstanciaPK[ idConstancia=" + idConstancia + ", idUsuario=" + idUsuario + " ]";
    }
    
}
