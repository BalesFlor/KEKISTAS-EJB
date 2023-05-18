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
@Table(name = "TIPO_CONSTANCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoConstancia.findAll", query = "SELECT t FROM TipoConstancia t"),
    @NamedQuery(name = "TipoConstancia.findByIdTipoConstancia", query = "SELECT t FROM TipoConstancia t WHERE t.idTipoConstancia = :idTipoConstancia"),
    @NamedQuery(name = "TipoConstancia.findByNomTipoConstancia", query = "SELECT t FROM TipoConstancia t WHERE t.nomTipoConstancia = :nomTipoConstancia")})
public class TipoConstancia implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOM_TIPO_CONSTANCIA")
    private String nomTipoConstancia;

    private static final long serialVersionUID = 1L;
    @Id  @GeneratedValue(generator = "tipo_constancia_seq")
    @SequenceGenerator(name = "tipo_constancia_seq", sequenceName = "TIPO_CONSTANCIA_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_CONSTANCIA")
    private BigInteger idTipoConstancia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoConstancia")
    private List<Constancia> constanciaList;

    public TipoConstancia() {
    }

    public TipoConstancia(BigInteger idTipoConstancia) {
        this.idTipoConstancia = idTipoConstancia;
    }

    public TipoConstancia(BigInteger idTipoConstancia, String nomTipoConstancia) {
        this.idTipoConstancia = idTipoConstancia;
        this.nomTipoConstancia = nomTipoConstancia;
    }

    public BigInteger getIdTipoConstancia() {
        return idTipoConstancia;
    }

    public void setIdTipoConstancia(BigInteger idTipoConstancia) {
        this.idTipoConstancia = idTipoConstancia;
    }

    public String getNomTipoConstancia() {
        return nomTipoConstancia;
    }

    public void setNomTipoConstancia(String nomTipoConstancia) {
        this.nomTipoConstancia = nomTipoConstancia;
    }

    @XmlTransient
    public List<Constancia> getConstanciaList() {
        return constanciaList;
    }

    public void setConstanciaList(List<Constancia> constanciaList) {
        this.constanciaList = constanciaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoConstancia != null ? idTipoConstancia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoConstancia)) {
            return false;
        }
        TipoConstancia other = (TipoConstancia) object;
        if ((this.idTipoConstancia == null && other.idTipoConstancia != null) || (this.idTipoConstancia != null && !this.idTipoConstancia.equals(other.idTipoConstancia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.TipoConstancia[ idTipoConstancia=" + idTipoConstancia + " ]";
    }
}