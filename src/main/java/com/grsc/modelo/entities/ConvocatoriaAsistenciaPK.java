package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ConvocatoriaAsistenciaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EVENTO")
    private BigInteger idEvento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;

    public ConvocatoriaAsistenciaPK() {
    }

    public ConvocatoriaAsistenciaPK(BigInteger idEvento, BigInteger idUsuario) {
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(BigInteger idEvento) {
        this.idEvento = idEvento;
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
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvocatoriaAsistenciaPK)) {
            return false;
        }
        ConvocatoriaAsistenciaPK other = (ConvocatoriaAsistenciaPK) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.ConvocatoriaAsistenciaPK[ idEvento=" + idEvento + ", idUsuario=" + idUsuario + " ]";
    }
    
}
