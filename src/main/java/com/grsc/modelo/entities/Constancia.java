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
import javax.persistence.GenerationType;
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
@Table(name = "CONSTANCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Constancia.findAll", query = "SELECT c FROM Constancia c"),
    @NamedQuery(name = "Constancia.findByIdConstancia", query = "SELECT c FROM Constancia c WHERE c.idConstancia = :idConstancia"),
    @NamedQuery(name = "Constancia.findByFechaHora", query = "SELECT c FROM Constancia c WHERE c.fechaHora = :fechaHora"),
    @NamedQuery(name = "Constancia.findByDetalle", query = "SELECT c FROM Constancia c WHERE c.detalle = :detalle")})
public class Constancia implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_HORA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Size(max = 250)
    @Column(name = "DETALLE")
    private String detalle;

    private static final long serialVersionUID = 1L;
    @Id  
    @GeneratedValue(generator = "constancia_seq")
    @SequenceGenerator(name = "constancia_seq", sequenceName = "CONSTANCIA_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CONSTANCIA")
    private BigInteger idConstancia;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO", unique = false, nullable = true)
    @ManyToOne
    private EstadoPeticion idEstadoPeticion;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne(optional = false)
    private Estudiante idUsuario;
    @JoinColumn(name = "ID_TIPO_CONSTANCIA", referencedColumnName = "ID_TIPO_CONSTANCIA")
    @ManyToOne(optional = false)
    private TipoConstancia idTipoConstancia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constancia")
    private List<AccionConstancia> accionConstanciaList;

    public Constancia() {
    }

    public Constancia(BigInteger idConstancia) {
        this.idConstancia = idConstancia;
    }

    public Constancia(BigInteger idConstancia, Date fechaHora) {
        this.idConstancia = idConstancia;
        this.fechaHora = fechaHora;
    }

    public BigInteger getIdConstancia() {
        return idConstancia;
    }

    public void setIdConstancia(BigInteger idConstancia) {
        this.idConstancia = idConstancia;
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

    public TipoConstancia getIdTipoConstancia() {
        return idTipoConstancia;
    }

    public void setIdTipoConstancia(TipoConstancia idTipoConstancia) {
        this.idTipoConstancia = idTipoConstancia;
    }

    @XmlTransient
    public List<AccionConstancia> getAccionConstanciaList() {
        return accionConstanciaList;
    }

    public void setAccionConstanciaList(List<AccionConstancia> accionConstanciaList) {
        this.accionConstanciaList = accionConstanciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConstancia != null ? idConstancia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Constancia)) {
            return false;
        }
        Constancia other = (Constancia) object;
        if ((this.idConstancia == null && other.idConstancia != null) || (this.idConstancia != null && !this.idConstancia.equals(other.idConstancia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Constancia[ idConstancia=" + idConstancia + " ]";
    }

}
