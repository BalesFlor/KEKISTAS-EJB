package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "GENERACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generacion.findAll", query = "SELECT g FROM Generacion g"),
    @NamedQuery(name = "Generacion.findByNomGeneracion", query = "SELECT g FROM Generacion g WHERE g.nomGeneracion = :nomGeneracion"),
    @NamedQuery(name = "Generacion.findByAnio", query = "SELECT g FROM Generacion g WHERE g.anio = :anio")})
public class Generacion implements Serializable {

    @Size(max = 150)
    @Column(name = "NOM_GENERACION")
    private String nomGeneracion;

    private static final long serialVersionUID = 1L;
    @Id   
    @GeneratedValue(generator = "generacion_seq")
    @SequenceGenerator(name = "generacion_seq", sequenceName = "GENERACION_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANIO")
    private BigInteger anio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anioGen")
    private List<Estudiante> estudianteList;

    public Generacion() {
    }

    public Generacion(BigInteger anio) {
        this.anio = anio;
    }
    
    public Generacion(String nomGeneracion, BigInteger anio) {
        this.nomGeneracion=nomGeneracion;
        this.anio = anio;
    }

    public String getNomGeneracion() {
        return nomGeneracion;
    }

    public void setNomGeneracion(String nomGeneracion) {
        this.nomGeneracion = nomGeneracion;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anio != null ? anio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Generacion)) {
            return false;
        }
        Generacion other = (Generacion) object;
        if ((this.anio == null && other.anio != null) || (this.anio != null && !this.anio.equals(other.anio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Generacion[ anio=" + anio + " ]";
    }
}