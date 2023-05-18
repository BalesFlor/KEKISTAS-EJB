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
@Table(name = "ACCION_JUSTIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccionJustificacion.findAll", query = "SELECT a FROM AccionJustificacion a"),
    @NamedQuery(name = "AccionJustificacion.findByFechaHora", query = "SELECT a FROM AccionJustificacion a WHERE a.fechaHora = :fechaHora"),
    @NamedQuery(name = "AccionJustificacion.findByDetalle", query = "SELECT a FROM AccionJustificacion a WHERE a.detalle = :detalle"),
    @NamedQuery(name = "AccionJustificacion.findByIdJustificacion", query = "SELECT a FROM AccionJustificacion a WHERE a.accionJustificacionPK.idJustificacion = :idJustificacion"),
    @NamedQuery(name = "AccionJustificacion.findByIdUsuario", query = "SELECT a FROM AccionJustificacion a WHERE a.accionJustificacionPK.idUsuario = :idUsuario")})
public class AccionJustificacion implements Serializable {

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
    protected AccionJustificacionPK accionJustificacionPK;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Analista analista;
    @JoinColumn(name = "ID_JUSTIFICACION", referencedColumnName = "ID_JUSTIFICACION", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Justificacion justificacion;

    public AccionJustificacion() {
    }

    public AccionJustificacion(AccionJustificacionPK accionJustificacionPK) {
        this.accionJustificacionPK = accionJustificacionPK;
    }

    public AccionJustificacion(AccionJustificacionPK accionJustificacionPK, Date fechaHora, String detalle) {
        this.accionJustificacionPK = accionJustificacionPK;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public AccionJustificacion(BigInteger idJustificacion, BigInteger idUsuario) {
        this.accionJustificacionPK = new AccionJustificacionPK(idJustificacion, idUsuario);
    }

    public AccionJustificacionPK getAccionJustificacionPK() {
        return accionJustificacionPK;
    }

    public void setAccionJustificacionPK(AccionJustificacionPK accionJustificacionPK) {
        this.accionJustificacionPK = accionJustificacionPK;
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

    public Justificacion getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(Justificacion justificacion) {
        this.justificacion = justificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accionJustificacionPK != null ? accionJustificacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccionJustificacion)) {
            return false;
        }
        AccionJustificacion other = (AccionJustificacion) object;
        if ((this.accionJustificacionPK == null && other.accionJustificacionPK != null) || (this.accionJustificacionPK != null && !this.accionJustificacionPK.equals(other.accionJustificacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.AccionJustificacion[ accionJustificacionPK=" + accionJustificacionPK + " ]";
    }

}
