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
@Table(name = "ROLES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Roles.findAll", query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.findByIdRol", query = "SELECT r FROM Roles r WHERE r.idRol = :idRol"),
    @NamedQuery(name = "Roles.findByNombre", query = "SELECT r FROM Roles r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Roles.findByDescripcion", query = "SELECT r FROM Roles r WHERE r.descripcion = :descripcion")})
public class Roles implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    private static final long serialVersionUID = 1L;
    @Id   @GeneratedValue(generator = "roles_seq")
    @SequenceGenerator(name = "roles_seq", sequenceName = "ROLES_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ROL")
    private BigInteger idRol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Rol")
    private List<Usuarios> usuariosList;

    public Roles() {
    }

    public Roles(BigInteger idRol) {
        this.idRol = idRol;
    }

    public Roles(BigInteger idRol, String nombre) {
        this.idRol = idRol;
        this.nombre = nombre;
    }

    public BigInteger getIdRol() {
        return idRol;
    }

    public void setIdRol(BigInteger idRol) {
        this.idRol = idRol;
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
        hash += (idRol != null ? idRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Roles)) {
            return false;
        }
        Roles other = (Roles) object;
        if ((this.idRol == null && other.idRol != null) || (this.idRol != null && !this.idRol.equals(other.idRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Roles[ idRol=" + idRol + " ]";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
