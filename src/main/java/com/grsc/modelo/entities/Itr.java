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
@Table(name = "ITR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Itr.findAll", query = "SELECT i FROM Itr i"),
    @NamedQuery(name = "Itr.findByIdItr", query = "SELECT i FROM Itr i WHERE i.idItr = :idItr"),
    @NamedQuery(name = "Itr.findByNomItr", query = "SELECT i FROM Itr i WHERE i.nomItr = :nomItr")})
public class Itr implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOM_ITR")
    private String nomItr;

    private static final long serialVersionUID = 1L;
    @Id  @GeneratedValue(generator = "itr_seq")
    @SequenceGenerator(name = "itr_seq", sequenceName = "ITR_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ITR")
    private BigInteger idItr;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO")
    @ManyToOne(optional = false)
    private Departamento idDepartamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Itr")
    private List<Usuarios> usuariosList;

    public Itr() {
    }

    public Itr(BigInteger idItr) {
        this.idItr = idItr;
    }

    public Itr(BigInteger idItr, String nomItr) {
        this.idItr = idItr;
        this.nomItr = nomItr;
    }

    public BigInteger getIdItr() {
        return idItr;
    }

    public void setIdItr(BigInteger idItr) {
        this.idItr = idItr;
    }

    public String getNomItr() {
        return nomItr;
    }

    public void setNomItr(String nomItr) {
        this.nomItr = nomItr;
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
        hash += (idItr != null ? idItr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Itr)) {
            return false;
        }
        Itr other = (Itr) object;
        if ((this.idItr == null && other.idItr != null) || (this.idItr != null && !this.idItr.equals(other.idItr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Itr[ idItr=" + idItr + " ]";
    }

    
   
}