package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "AREA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Area.findAll", query = "SELECT a FROM Area a"),
    @NamedQuery(name = "Area.findByIdArea", query = "SELECT a FROM Area a WHERE a.idArea = :idArea"),
    @NamedQuery(name = "Area.findByNomArea", query = "SELECT a FROM Area a WHERE a.nomArea = :nomArea")})
public class Area implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NOM_AREA")
    private String nomArea;

    private static final long serialVersionUID = 1L;
    @Id  
    @SequenceGenerator(name = "AREA_SEQ", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_AREA")
    private BigInteger idArea;
    @OneToMany(mappedBy = "idArea")
    private List<Tutor> tutorList;

    public Area() {
    }

    public Area(BigInteger idArea) {
        this.idArea = idArea;
    }

    public Area(BigInteger idArea, String nomArea) {
        this.idArea = idArea;
        this.nomArea = nomArea;
    }

    public BigInteger getIdArea() {
        return idArea;
    }

    public void setIdArea(BigInteger idArea) {
        this.idArea = idArea;
    }

    public String getNomArea() {
        return nomArea;
    }

    public void setNomArea(String nomArea) {
        this.nomArea = nomArea;
    }

    @XmlTransient
    public List<Tutor> getTutorList() {
        return tutorList;
    }

    public void setTutorList(List<Tutor> tutorList) {
        this.tutorList = tutorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArea != null ? idArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.idArea == null && other.idArea != null) || (this.idArea != null && !this.idArea.equals(other.idArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Area[ idArea=" + idArea + " ]";
    }

}
