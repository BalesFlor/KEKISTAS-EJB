package com.grsc.modelo.entities;

import com.sun.istack.Nullable;
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
@Table(name = "RECLAMO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reclamo.findAll", query = "SELECT r FROM Reclamo r"),
    @NamedQuery(name = "Reclamo.findByIdReclamo", query = "SELECT r FROM Reclamo r WHERE r.idReclamo = :idReclamo"),
    @NamedQuery(name = "Reclamo.findByFechaHora", query = "SELECT r FROM Reclamo r WHERE r.fechaHora = :fechaHora"),
    @NamedQuery(name = "Reclamo.findByIdUsuarioFecha", query = "SELECT r FROM Reclamo r WHERE r.idUsuario = :idUsuario AND r.fechaHora = :fechaHora AND r.titulo = :titulo"),
    @NamedQuery(name = "Reclamo.findByDetalle", query = "SELECT r FROM Reclamo r WHERE r.detalle = :detalle"),
    @NamedQuery(name = "Reclamo.findByTitulo", query = "SELECT r FROM Reclamo r WHERE r.titulo = :titulo"),
    @NamedQuery(name = "Reclamo.findBySemestre", query = "SELECT r FROM Reclamo r WHERE r.semestre = :semestre"),
    @NamedQuery(name = "Reclamo.findByFecha", query = "SELECT r FROM Reclamo r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "Reclamo.findByCreditos", query = "SELECT r FROM Reclamo r WHERE r.creditos = :creditos")})
public class Reclamo implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "TITULO")
    private String titulo;

    private static final long serialVersionUID = 1L;
    @Id  @GeneratedValue(generator = "reclamo_seq")
    @SequenceGenerator(name = "reclamo_seq", sequenceName = "RECLAMO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RECLAMO")
    private BigInteger idReclamo;
    @Column(name = "SEMESTRE")
    private BigInteger semestre;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "CREDITOS")
    private BigInteger creditos;
    @JoinColumn(name = "ID_ESTADO_PETICION", referencedColumnName = "ID_ESTADO")
    @ManyToOne(optional = false)
    private EstadoPeticion idEstadoPeticion;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID_EVENTO")
    @ManyToOne(optional = false)
    @Nullable
    private Evento idEvento;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Estudiante idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reclamo")
    private List<AccionReclamo> accionReclamoList;

    public Reclamo() {
    }

    public Reclamo(BigInteger idReclamo) {
        this.idReclamo = idReclamo;
    }

    public Reclamo(BigInteger idReclamo, Date fechaHora, String detalle, String titulo) {
        this.idReclamo = idReclamo;
        this.fechaHora = fechaHora;
        this.detalle = detalle;
        this.titulo = titulo;
    }

    public BigInteger getIdReclamo() {
        return idReclamo;
    }

    public Reclamo(Date fechaHora, String detalle, String titulo, BigInteger semestre, Date fecha, BigInteger creditos, EstadoPeticion idEstadoPeticion, Evento idEvento, Estudiante idUsuario) {
        this.fechaHora = fechaHora;
        this.detalle = detalle;
        this.titulo = titulo;
        this.semestre = semestre;
        this.fecha = fecha;
        this.creditos = creditos;
        this.idEstadoPeticion = idEstadoPeticion;
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
    }

    public void setIdReclamo(BigInteger idReclamo) {
        this.idReclamo = idReclamo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }


    public BigInteger getSemestre() {
        return semestre;
    }

    public void setSemestre(BigInteger semestre) {
        this.semestre = semestre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getCreditos() {
        return creditos;
    }

    public void setCreditos(BigInteger creditos) {
        this.creditos = creditos;
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

    @XmlTransient
    public List<AccionReclamo> getAccionReclamoList() {
        return accionReclamoList;
    }

    public void setAccionReclamoList(List<AccionReclamo> accionReclamoList) {
        this.accionReclamoList = accionReclamoList;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Evento getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Evento idEvento) {
        this.idEvento = idEvento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReclamo != null ? idReclamo.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reclamo)) {
            return false;
        }
        Reclamo other = (Reclamo) object;
        if ((this.idReclamo == null && other.idReclamo != null) || (this.idReclamo != null && !this.idReclamo.equals(other.idReclamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Reclamo[ idReclamo=" + idReclamo + " ]";
    }

}
