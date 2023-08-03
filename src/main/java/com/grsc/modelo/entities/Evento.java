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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "EVENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
    @NamedQuery(name = "Evento.findByIdEvento", query = "SELECT e FROM Evento e WHERE e.idEvento = :idEvento"),
    @NamedQuery(name = "Evento.findByFechaHoraInicio", query = "SELECT e FROM Evento e WHERE e.fechaHoraInicio = :fechaHoraInicio"),
    @NamedQuery(name = "Evento.findByFechasTitulo", query = "SELECT e FROM Evento e WHERE e.fechaHoraInicio = :fechaHoraInicio AND e.titulo = :titulo"),
    @NamedQuery(name = "Evento.findByFechaHoraFin", query = "SELECT e FROM Evento e WHERE e.fechaHoraFin = :fechaHoraFin"),
    @NamedQuery(name = "Evento.findByTitulo", query = "SELECT e FROM Evento e WHERE e.titulo = :titulo")})
public class Evento implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "TITULO")
    private String titulo;
    @JoinColumn(name = "TIPO_EVENTO", referencedColumnName = "ID")
    @ManyToOne
    private TipoEvento tipoEvento;
    @JoinColumn(name = "ID_TUTOR", referencedColumnName = "ID_USUARIO")
    @ManyToOne
    private Tutor tutor;

    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(generator = "evento_seq")
    @SequenceGenerator(name = "evento_seq", sequenceName = "EVENTO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EVENTO")
    private BigInteger idEvento;
    @JoinTable(name = "GESTION", joinColumns = {
        @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID_EVENTO")}, inverseJoinColumns = {
        @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")})
    @ManyToMany
    private List<Analista> analistaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evento")
    private List<ConvocatoriaAsistencia> convocatoriaAsistenciaList;
    

    public Evento() {
    }

    public Evento(BigInteger idEvento) {
        this.idEvento = idEvento;
    }

    public Evento(BigInteger idEvento, Date fechaHoraInicio, Date fechaHoraFin, String titulo) {
        this.idEvento = idEvento;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.titulo = titulo;
    }

    public BigInteger getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(BigInteger idEvento) {
        this.idEvento = idEvento;
    }

    public Date getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(Date fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public Date getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(Date fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }


    @XmlTransient
    public List<Analista> getAnalistaList() {
        return analistaList;
    }

    public void setAnalistaList(List<Analista> analistaList) {
        this.analistaList = analistaList;
    }

    @XmlTransient
    public List<ConvocatoriaAsistencia> getConvocatoriaAsistenciaList() {
        return convocatoriaAsistenciaList;
    }

    public void setConvocatoriaAsistenciaList(List<ConvocatoriaAsistencia> convocatoriaAsistenciaList) {
        this.convocatoriaAsistenciaList = convocatoriaAsistenciaList;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Evento[ idEvento=" + idEvento + " ]";
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}