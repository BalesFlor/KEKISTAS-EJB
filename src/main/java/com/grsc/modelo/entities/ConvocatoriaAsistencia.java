package com.grsc.modelo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.AllArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "CONVOCATORIA_ASISTENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConvocatoriaAsistencia.findAll", query = "SELECT c FROM ConvocatoriaAsistencia c"),
    @NamedQuery(name = "ConvocatoriaAsistencia.findByCalificacion", query = "SELECT c FROM ConvocatoriaAsistencia c WHERE c.calificacion = :calificacion"),
    @NamedQuery(name = "ConvocatoriaAsistencia.findByAsistencia", query = "SELECT c FROM ConvocatoriaAsistencia c WHERE c.asistencia = :asistencia"),
    @NamedQuery(name = "ConvocatoriaAsistencia.findByIdEvento", query = "SELECT c FROM ConvocatoriaAsistencia c WHERE c.convocatoriaAsistenciaPK.idEvento = :idEvento"),
    @NamedQuery(name = "ConvocatoriaAsistencia.findByIdUsuario", query = "SELECT c FROM ConvocatoriaAsistencia c WHERE c.convocatoriaAsistenciaPK.idUsuario = :idUsuario")})
public class ConvocatoriaAsistencia implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CALIFICACION")
    private BigInteger calificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASISTENCIA")
    private Character asistencia;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConvocatoriaAsistenciaPK convocatoriaAsistenciaPK;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID_EVENTO", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Evento evento;

    public ConvocatoriaAsistencia() {
    }

    public ConvocatoriaAsistencia(ConvocatoriaAsistenciaPK convocatoriaAsistenciaPK) {
        this.convocatoriaAsistenciaPK = convocatoriaAsistenciaPK;
    }

    public ConvocatoriaAsistencia(ConvocatoriaAsistenciaPK convocatoriaAsistenciaPK, BigInteger calificacion, Character asistencia) {
        this.convocatoriaAsistenciaPK = convocatoriaAsistenciaPK;
        this.calificacion = calificacion;
        this.asistencia = asistencia;
    }

    public ConvocatoriaAsistencia(BigInteger idEvento, BigInteger idUsuario) {
        this.convocatoriaAsistenciaPK = new ConvocatoriaAsistenciaPK(idEvento, idUsuario);
    }

    public ConvocatoriaAsistenciaPK getConvocatoriaAsistenciaPK() {
        return convocatoriaAsistenciaPK;
    }

    public void setConvocatoriaAsistenciaPK(ConvocatoriaAsistenciaPK convocatoriaAsistenciaPK) {
        this.convocatoriaAsistenciaPK = convocatoriaAsistenciaPK;
    }


    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (convocatoriaAsistenciaPK != null ? convocatoriaAsistenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConvocatoriaAsistencia)) {
            return false;
        }
        ConvocatoriaAsistencia other = (ConvocatoriaAsistencia) object;
        if ((this.convocatoriaAsistenciaPK == null && other.convocatoriaAsistenciaPK != null) || (this.convocatoriaAsistenciaPK != null && !this.convocatoriaAsistenciaPK.equals(other.convocatoriaAsistenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.grsc.modelo.entities.ConvocatoriaAsistencia[ convocatoriaAsistenciaPK=" + convocatoriaAsistenciaPK + " ]";
    }

    public BigInteger getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(BigInteger calificacion) {
        this.calificacion = calificacion;
    }

    public Character getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Character asistencia) {
        this.asistencia = asistencia;
    }
    
}
