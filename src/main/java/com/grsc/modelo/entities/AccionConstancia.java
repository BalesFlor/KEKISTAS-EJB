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
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "ACCION_CONSTANCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccionConstancia.findAll", query = "SELECT a FROM AccionConstancia a"),
    @NamedQuery(name = "AccionConstancia.findByFechaHora", query = "SELECT a FROM AccionConstancia a WHERE a.fechaHora = :fechaHora"),
    @NamedQuery(name = "AccionConstancia.findByDetalle", query = "SELECT a FROM AccionConstancia a WHERE a.detalle = :detalle"),
    @NamedQuery(name = "AccionConstancia.findByIdConstancia", query = "SELECT a FROM AccionConstancia a WHERE a.accionConstanciaPK.idConstancia = :idConstancia"),
    @NamedQuery(name = "AccionConstancia.findByIdUsuario", query = "SELECT a FROM AccionConstancia a WHERE a.accionConstanciaPK.idUsuario = :idUsuario")})
public class AccionConstancia implements Serializable {

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
    protected AccionConstanciaPK accionConstanciaPK;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Analista analista;
    @JoinColumn(name = "ID_CONSTANCIA", referencedColumnName = "ID_CONSTANCIA", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Constancia constancia;

    public AccionConstancia() {
    }

    public AccionConstancia(AccionConstanciaPK accionConstanciaPK) {
        this.accionConstanciaPK = accionConstanciaPK;
    }

    public AccionConstancia(AccionConstanciaPK accionConstanciaPK, Date fechaHora, String detalle) {
        this.accionConstanciaPK = accionConstanciaPK;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public AccionConstancia(BigInteger idConstancia, BigInteger idUsuario) {
        this.accionConstanciaPK = new AccionConstanciaPK(idConstancia, idUsuario);
    }

    public AccionConstanciaPK getAccionConstanciaPK() {
        return accionConstanciaPK;
    }

    public void setAccionConstanciaPK(AccionConstanciaPK accionConstanciaPK) {
        this.accionConstanciaPK = accionConstanciaPK;
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

    public Constancia getConstancia() {
        return constancia;
    }

    public void setConstancia(Constancia constancia) {
        this.constancia = constancia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accionConstanciaPK != null ? accionConstanciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccionConstancia)) {
            return false;
        }
        AccionConstancia other = (AccionConstancia) object;
        if ((this.accionConstanciaPK == null && other.accionConstanciaPK != null) || (this.accionConstanciaPK != null && !this.accionConstanciaPK.equals(other.accionConstanciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionConstancia[ accionConstanciaPK=" + accionConstanciaPK + " ]";
    }

}
