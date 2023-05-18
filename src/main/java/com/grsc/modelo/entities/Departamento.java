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
@Table(name = "DEPARTAMENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findByIdDepartamento", query = "SELECT d FROM Departamento d WHERE d.idDepartamento = :idDepartamento"),
    @NamedQuery(name = "Departamento.findByNomDepartamento", query = "SELECT d FROM Departamento d WHERE d.nomDepartamento = :nomDepartamento")})
public class Departamento implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "NOM_DEPARTAMENTO")
    private String nomDepartamento;

    private static final long serialVersionUID = 1L;
    @Id  
    @GeneratedValue(generator = "departamento_seq")
    @SequenceGenerator(name = "departamento_seq", sequenceName = "DEPARTAMENTO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_DEPARTAMENTO")
    private BigInteger idDepartamento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDepartamento")
    private List<Localidad> localidadList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDepartamento")
    private List<Itr> itrList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamento")
    private List<Usuarios> usuariosList;

    public Departamento() {
    }

    public Departamento(BigInteger idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Departamento(BigInteger idDepartamento, String nomDepartamento) {
        this.idDepartamento = idDepartamento;
        this.nomDepartamento = nomDepartamento;
    }

    public BigInteger getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(BigInteger idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNomDepartamento() {
        return nomDepartamento;
    }

    public void setNomDepartamento(String nomDepartamento) {
        this.nomDepartamento = nomDepartamento;
    }

    @XmlTransient
    public List<Localidad> getLocalidadList() {
        return localidadList;
    }

    public void setLocalidadList(List<Localidad> localidadList) {
        this.localidadList = localidadList;
    }

    @XmlTransient
    public List<Itr> getItrList() {
        return itrList;
    }

    public void setItrList(List<Itr> itrList) {
        this.itrList = itrList;
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
        hash += (idDepartamento != null ? idDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.idDepartamento == null && other.idDepartamento != null) || (this.idDepartamento != null && !this.idDepartamento.equals(other.idDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Departamento[ idDepartamento=" + idDepartamento + " ]";
    }

}
