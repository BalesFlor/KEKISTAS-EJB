package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Logger;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "USUARIOS")
@XmlRootElement
@NamedQueries({
  
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByIdUsuario", query = "SELECT u FROM Usuarios u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuarios.findByNomUsuario", query = "SELECT u FROM Usuarios u WHERE u.nomUsuario = :nomUsuario"),
    @NamedQuery(name = "Usuarios.findByNomUsuarioAndPassword", query = "SELECT u FROM Usuarios u WHERE u.nomUsuario = :nomUsuario AND u.contrasenia= :contrasenia"),
    @NamedQuery(name = "Usuarios.findByApellido1", query = "SELECT u FROM Usuarios u WHERE u.apellido1 = :apellido1"),
    @NamedQuery(name = "Usuarios.findByNombre2", query = "SELECT u FROM Usuarios u WHERE u.nombre2 = :nombre2"),
    @NamedQuery(name = "Usuarios.findByNombre1", query = "SELECT u FROM Usuarios u WHERE u.nombre1 = :nombre1"),
    @NamedQuery(name = "Usuarios.findByFecNac", query = "SELECT u FROM Usuarios u WHERE u.fecNac = :fecNac"),
    @NamedQuery(name = "Usuarios.findByContrase\u00f1a", query = "SELECT u FROM Usuarios u WHERE u.contrasenia = :contrasenia"),
    @NamedQuery(name = "Usuarios.findByGenero", query = "SELECT u FROM Usuarios u WHERE u.genero = :genero"),
    @NamedQuery(name = "Usuarios.findByMailInstitucional", query = "SELECT u FROM Usuarios u WHERE u.mailInstitucional = :mailInstitucional"),
    @NamedQuery(name = "Usuarios.findByTelefono", query = "SELECT u FROM Usuarios u WHERE u.telefono = :telefono"),
    @NamedQuery(name = "Usuarios.findByDocumento", query = "SELECT u FROM Usuarios u WHERE u.documento = :documento"),
    @NamedQuery(name = "Usuarios.findByApellido2", query = "SELECT u FROM Usuarios u WHERE u.apellido2 = :apellido2"),
    @NamedQuery(name = "Usuarios.findByMailPersonal", query = "SELECT u FROM Usuarios u WHERE u.mailPersonal = :mailPersonal")})
public class Usuarios implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOM_USUARIO")
    private String nomUsuario;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 50)
    @Column(name = "APELLIDO1")
    private String apellido1;
    @Size(max = 30)
    @Column(name = "NOMBRE2")
    private String nombre2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "NOMBRE1")
    private String nombre1;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "FEC_NAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecNac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CONTRASENIA")
    private String contrasenia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GENERO")
    private Character genero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MAIL_INSTITUCIONAL")
    private String mailInstitucional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "TELEFONO")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "DOCUMENTO")
    private String documento;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 50)
    @Column(name = "APELLIDO2")
    private String apellido2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MAIL_PERSONAL")
    private String mailPersonal;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID_ESTADO", unique = false, nullable = true)
    @ManyToOne
    private EstadoUsuario idEstadoUsuario;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuarios")
    private Tutor tutor;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuarios")
    private Analista analista;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID_DEPARTAMENTO")
    @ManyToOne(optional = false)
    private Departamento departamento;
    @JoinColumn(name = "ID_ITR", referencedColumnName = "ID_ITR")
    @ManyToOne(optional = false)
    private Itr Itr;
    @JoinColumn(name = "ID_LOCALIDAD", referencedColumnName = "ID_LOCALIDAD")
    @ManyToOne
    private Localidad Localidad;
    @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL")
    @ManyToOne(optional = false)
    private Roles Rol;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuarios")
    private Estudiante estudiante;


    public Usuarios() {
    }

    public Usuarios(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuarios(BigInteger idUsuario, String nomUsuario, String apellido1, String nombre1, Date fecNac, String contrasenia, Character genero, String MailInstitucional, String telefono, String documento, String mailPersonal) {
        this.idUsuario = idUsuario;
        this.nomUsuario = nomUsuario;
        this.apellido1 = apellido1;
        this.nombre1 = nombre1;
        this.fecNac = fecNac;
        this.contrasenia = contrasenia;
        this.genero = genero;
        this.mailInstitucional = MailInstitucional;
        this.telefono = telefono;
        this.documento = documento;
        this.mailPersonal = mailPersonal;
    }

    public Usuarios(String nomUsuario, String apellido1, String nombre2, String nombre1, Date fecNac, String contrasenia, Character genero, String mailInstitucional, String telefono, String documento, String apellido2, String mailPersonal, EstadoUsuario idEstadoUsuario,  Departamento departamento, Itr Itr, Localidad Localidad, Roles Rol) {
        this.nomUsuario = nomUsuario;
        this.apellido1 = apellido1;
        this.nombre2 = nombre2;
        this.nombre1 = nombre1;
        this.fecNac = fecNac;
        this.contrasenia = contrasenia;
        this.genero = genero;
        this.mailInstitucional = mailInstitucional;
        this.telefono = telefono;
        this.documento = documento;
        this.apellido2 = apellido2;
        this.mailPersonal = mailPersonal;
        this.idEstadoUsuario = idEstadoUsuario;
        this.departamento = departamento;
        this.Itr = Itr;
        this.Localidad = Localidad;
        this.Rol = Rol;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    
    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    } 
    
    public Date getFecNac() {
        return fecNac;
    }

    public void setFecNac(Date fecNac) {
        this.fecNac = fecNac;
    }

    public String getMailInstitucional() {
        return mailInstitucional;
    }

    public void setMailInstitucional(String MailInstitucional) {
        this.mailInstitucional = MailInstitucional;
    }

    public String getMailPersonal() {
        return mailPersonal;
    }

    public void setMailPersonal(String mailPersonal) {
        this.mailPersonal = mailPersonal;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Analista getAnalista() {
        return analista;
    }

    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Itr getItr() {
        return Itr;
    }

    public void setItr(Itr Itr) {
        this.Itr = Itr;
    }

    public Localidad getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(Localidad Localidad) {
        this.Localidad = Localidad;
    }

    public Roles getRol() {
        return Rol;
    }

    public void setRol(Roles Rol) {
        this.Rol = Rol;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public EstadoUsuario getIdEstadoUsuario() {
        return idEstadoUsuario;
    }

    public void setIdEstadoUsuario(EstadoUsuario idEstadoUsuario) {
        this.idEstadoUsuario = idEstadoUsuario;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        return !((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario)));
    }
    
    @Override
    public String toString() {
        return "com.grsc.modelo.entities.Usuarios[ idUsuario=" + idUsuario + " ]";
    }

    
}
