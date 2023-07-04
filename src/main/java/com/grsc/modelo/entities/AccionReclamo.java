package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "ACCION_RECLAMO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccionReclamo.findAll", query = "SELECT a FROM AccionReclamo a"),
    @NamedQuery(name = "AccionReclamo.findByFechaHora", query = "SELECT a FROM AccionReclamo a WHERE a.fechaHora = :fechaHora"),
    @NamedQuery(name = "AccionReclamo.findByDetalle", query = "SELECT a FROM AccionReclamo a WHERE a.detalle = :detalle"),
    @NamedQuery(name = "AccionReclamo.findByIdReclamo", query = "SELECT a FROM AccionReclamo a WHERE a.accionReclamoPK.idReclamo = :idReclamo"),
    @NamedQuery(name = "AccionReclamo.findByIdUsuario", query = "SELECT a FROM AccionReclamo a WHERE a.accionReclamoPK.idUsuario = :idUsuario"),
    @NamedQuery(name = "AccionReclamo.findByIdUsuarioFechaHora", query = "SELECT a FROM AccionReclamo a WHERE a.accionReclamoPK.idUsuario = :idUsuario AND a.fechaHora = :fechaHora")})
public class AccionReclamo implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "DETALLE")
    private String detalle;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccionReclamoPK accionReclamoPK;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Analista analista;
    @JoinColumn(name = "ID_RECLAMO", referencedColumnName = "ID_RECLAMO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Reclamo reclamo;

    public AccionReclamo() {
    }

    public AccionReclamo(AccionReclamoPK accionReclamoPK) {
        this.accionReclamoPK = accionReclamoPK;
    }

    public AccionReclamo(AccionReclamoPK accionReclamoPK, Date fechaHora, String detalle) {
        this.accionReclamoPK = accionReclamoPK;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public AccionReclamo(BigInteger idReclamo, BigInteger idUsuario) {
        this.accionReclamoPK = new AccionReclamoPK(idReclamo, idUsuario);
    }

    public AccionReclamoPK getAccionReclamoPK() {
        return accionReclamoPK;
    }

    public void setAccionReclamoPK(AccionReclamoPK accionReclamoPK) {
        this.accionReclamoPK = accionReclamoPK;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }


    public Analista getAnalista() {
        return analista;
    }

    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accionReclamoPK != null ? accionReclamoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccionReclamo)) {
            return false;
        }
        AccionReclamo other = (AccionReclamo) object;
        if ((this.accionReclamoPK == null && other.accionReclamoPK != null) || (this.accionReclamoPK != null && !this.accionReclamoPK.equals(other.accionReclamoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionReclamo[ accionReclamoPK=" + accionReclamoPK + " ]";
    }

}
