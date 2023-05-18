package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "LOCALIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidad.findAll", query = "SELECT l FROM Localidad l"),
    @NamedQuery(name = "Localidad.findByIdLocalidad", query = "SELECT l FROM Localidad l WHERE l.idLocalidad = :idLocalidad"),
    @NamedQuery(name = "Localidad.findByIdDepartamento", query = "SELECT l FROM Localidad l WHERE l.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "Localidad.findByNomLocalidad", query = "SELECT l FROM Localidad l WHERE l.nomLocalidad = :nomLocalidad")})
public class Localidad implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOM_LOCALIDAD")
    private String nomLocalidad;

    private static final long serialVersionUID = 1L;
    @Id  @GeneratedValue(generator = "localidad_seq")
    @SequenceGenerator(name = "localidad_seq", sequenceName = "LOCALIDAD_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LOCALIDAD")
    private BigInteger idLocalidad;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO")
    @ManyToOne(optional = false)
    private Departamento idDepartamento;
    @OneToMany(mappedBy = "Localidad")
    private List<Usuarios> usuariosList;

    public Localidad() {
    }

    public Localidad(BigInteger idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public Localidad(BigInteger idLocalidad, String nomLocalidad) {
        this.idLocalidad = idLocalidad;
        this.nomLocalidad = nomLocalidad;
    }
    
    public Localidad(BigInteger idLocalidad, String nomLocalidad, Departamento Departamento) {
        this.idLocalidad = idLocalidad;
        this.nomLocalidad = nomLocalidad;
        this.idDepartamento=Departamento;
    }

    public BigInteger getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(BigInteger idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getNomLocalidad() {
        return nomLocalidad;
    }

    public void setNomLocalidad(String nomLocalidad) {
        this.nomLocalidad = nomLocalidad;
    }

    public Departamento getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Departamento idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    @XmlTransient
    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLocalidad != null ? idLocalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Localidad)) {
            return false;
        }
        Localidad other = (Localidad) object;
        if ((this.idLocalidad == null && other.idLocalidad != null) || (this.idLocalidad != null && !this.idLocalidad.equals(other.idLocalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Localidad[ idLocalidad=" + idLocalidad + " ]";
    }

}