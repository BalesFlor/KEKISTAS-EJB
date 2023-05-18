package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AccionJustificacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_JUSTIFICACION")
    private BigInteger idJustificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;

    public AccionJustificacionPK() {
    }

    public AccionJustificacionPK(BigInteger idJustificacion, BigInteger idUsuario) {
        this.idJustificacion = idJustificacion;
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(BigInteger idJustificacion) {
        this.idJustificacion = idJustificacion;
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
        hash += (idJustificacion != null ? idJustificacion.hashCode() : 0);
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccionJustificacionPK)) {
            return false;
        }
        AccionJustificacionPK other = (AccionJustificacionPK) object;
        if ((this.idJustificacion == null && other.idJustificacion != null) || (this.idJustificacion != null && !this.idJustificacion.equals(other.idJustificacion))) {
            return false;
        }
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionJustificacionPK[ idJustificacion=" + idJustificacion + ", idUsuario=" + idUsuario + " ]";
    }
    
}
