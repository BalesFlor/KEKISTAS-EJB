package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "JUSTIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Justificacion.findAll", query = "SELECT j FROM Justificacion j"),
    @NamedQuery(name = "Justificacion.findByIdJustificacion", query = "SELECT j FROM Justificacion j WHERE j.idJustificacion = :idJustificacion"),
    @NamedQuery(name = "Justificacion.findByFechaHora", query = "SELECT j FROM Justificacion j WHERE j.fechaHora = :fechaHora"),
    @NamedQuery(name = "Justificacion.findByHoraEventoUsuario", query = "SELECT j FROM Justificacion j WHERE j.fechaHora = :fechaHora AND j.idEvento = :idEvento AND j.idUsuario = :idUsuario"),
    @NamedQuery(name = "Justificacion.findByDetalle", query = "SELECT j FROM Justificacion j WHERE j.detalle = :detalle")})
public class Justificacion implements Serializable {

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
    @Id 
    @GeneratedValue(generator = "justificacion_seq")
    @SequenceGenerator(name = "justificacion_seq", sequenceName = "JUSTIFICACION_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_JUSTIFICACION")
    private BigInteger idJustificacion;
    @JoinColumn(name = "ID_ESTADO_PETICION", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false)
    private EstadoPeticion idEstadoPeticion;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID_EVENTO")
    @ManyToOne(optional = false)
    private Evento idEvento;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Estudiante idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "justificacion")
    private List<AccionJustificacion> accionJustificacionList;

    public Justificacion() {
    }

    public Justificacion(BigInteger idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public Justificacion(BigInteger idJustificacion, Date fechaHora, String detalle) {
        this.idJustificacion = idJustificacion;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
    }

    public Justificacion(Date fechaHora, String detalle,  Evento idEvento, Estudiante idUsuario) {
        this.fechaHora = fechaHora;
        this.detalle = detalle;
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
    }
    
    public BigInteger getIdJustificacion() {
        return idJustificacion;
    }

    public void setIdJustificacion(BigInteger idJustificacion) {
        this.idJustificacion = idJustificacion;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }


    public EstadoPeticion getIdEstadoPeticion() {
        return idEstadoPeticion;
    }

    public void setIdEstadoPeticion(EstadoPeticion idEstadoPeticion) {
        this.idEstadoPeticion = idEstadoPeticion;
    }

    public Estudiante getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Estudiante idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Evento getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Evento idEvento) {
        this.idEvento = idEvento;
    }
    
    
    
    @XmlTransient
    public List<AccionJustificacion> getAccionJustificacionList() {
        return accionJustificacionList;
    }

    public void setAccionJustificacionList(List<AccionJustificacion> accionJustificacionList) {
        this.accionJustificacionList = accionJustificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJustificacion != null ? idJustificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Justificacion)) {
            return false;
        }
        Justificacion other = (Justificacion) object;
        if ((this.idJustificacion == null && other.idJustificacion != null) || (this.idJustificacion != null && !this.idJustificacion.equals(other.idJustificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Justificacion[ idJustificacion=" + idJustificacion + " ]";
    }
}