package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "TIPO_TUTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTutor.findAll", query = "SELECT t FROM TipoTutor t"),
    @NamedQuery(name = "TipoTutor.findByIdTipoTutor", query = "SELECT t FROM TipoTutor t WHERE t.idTipoTutor = :idTipoTutor"),
    @NamedQuery(name = "TipoTutor.findByNomTipoTutor", query = "SELECT t FROM TipoTutor t WHERE t.nomTipoTutor = :nomTipoTutor")})
public class TipoTutor implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "NOM_TIPO_TUTOR")
    private String nomTipoTutor;

    private static final long serialVersionUID = 1L;
    @Id  @GeneratedValue(generator = "tipo_tutor_seq")
    @SequenceGenerator(name = "tipo_tutor_seq", sequenceName = "TIPO_TUTOR_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_TUTOR")
    private BigInteger idTipoTutor;
    @OneToMany(mappedBy = "idTipoTutor")
    private List<Tutor> tutorList;

    public TipoTutor() {
    }

    public TipoTutor(BigInteger idTipoTutor) {
        this.idTipoTutor = idTipoTutor;
    }

    public TipoTutor(BigInteger idTipoTutor, String nomTipoTutor) {
        this.idTipoTutor = idTipoTutor;
        this.nomTipoTutor = nomTipoTutor;
    }

    public BigInteger getIdTipoTutor() {
        return idTipoTutor;
    }

    public void setIdTipoTutor(BigInteger idTipoTutor) {
        this.idTipoTutor = idTipoTutor;
    }

    public String getNomTipoTutor() {
        return nomTipoTutor;
    }

    public void setNomTipoTutor(String nomTipoTutor) {
        this.nomTipoTutor = nomTipoTutor;
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
        hash += (idTipoTutor != null ? idTipoTutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoTutor)) {
            return false;
        }
        TipoTutor other = (TipoTutor) object;
        if ((this.idTipoTutor == null && other.idTipoTutor != null) || (this.idTipoTutor != null && !this.idTipoTutor.equals(other.idTipoTutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.TipoTutor[ idTipoTutor=" + idTipoTutor + " ]";
    }

}
