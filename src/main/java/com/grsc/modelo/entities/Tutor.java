package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "TUTOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tutor.findAll", query = "SELECT t FROM Tutor t"),
    @NamedQuery(name = "Tutor.findByIdUsuario", query = "SELECT t FROM Tutor t WHERE t.idUsuario = :idUsuario")})
public class Tutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id  
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    @JoinColumn(name = "ID_AREA", referencedColumnName = "ID_AREA")
    @ManyToOne
    private Area idArea;
    @JoinColumn(name = "ID_TIPO_TUTOR", referencedColumnName = "ID_TIPO_TUTOR")
    @ManyToOne
    private TipoTutor idTipoTutor;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @OneToOne(cascade=CascadeType.ALL,optional = false)
    private Usuarios usuarios;

    public Tutor() {
    }

    public Tutor(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Area getIdArea() {
        return idArea;
    }

    public void setIdArea(Area idArea) {
        this.idArea = idArea;
    }

    public TipoTutor getIdTipoTutor() {
        return idTipoTutor;
    }

    public void setIdTipoTutor(TipoTutor idTipoTutor) {
        this.idTipoTutor = idTipoTutor;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tutor)) {
            return false;
        }
        Tutor other = (Tutor) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Tutor[ idUsuario=" + idUsuario + " ]";
    }
    
}
