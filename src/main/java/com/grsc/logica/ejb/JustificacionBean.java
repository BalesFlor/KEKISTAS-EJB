package com.grsc.logica.ejb;

import com.grsc.modelo.daos.JustificacionJpaController;
import com.grsc.modelo.entities.Estudiante;
import com.grsc.modelo.entities.Evento;
import com.grsc.modelo.entities.Justificacion;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JustificacionBean implements JustificacionBeanRemote{

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("GRSCPU");
    private JustificacionJpaController controlador= new JustificacionJpaController(entityManagerFactory);
    
    @Override
    public Boolean hacerJustificacion(Date fechaHora, String detalle, Evento evento, Estudiante usuario) throws ParseException {
        
        boolean pudeCrear = false;

        if ( existeJustificacion(fechaHora, evento, usuario) ) {
            System.out.println("Justificacion con dicha documento ya registrado");
        } else {
            Justificacion jus = Justificacion.builder()
                            .idEvento(evento)
                            .idUsuario(usuario)
                            .fechaHora(fechaHora)
                            .detalle(detalle)
                            .build();

            try {
                controlador.create(jus);
                pudeCrear = true;
            } catch (Exception ex) {
                Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pudeCrear;
    }

    @Override
    public Justificacion buscarJustificacion(Date fechayHora, Evento evento, Estudiante user) {
        return controlador.findJustificacionUsuarioHoraEvento(fechayHora, evento, user);
    }

    @Override
    public Boolean existeJustificacion(Date fechayHora, Evento evento, Estudiante user) {
        Justificacion jus = buscarJustificacion(fechayHora, evento, user);
        return jus.getIdJustificacion()!=null;
    }
    
    @Override
    public Boolean eliminarJustificacion(BigInteger id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean modificarJustificacion(BigInteger id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Justificacion> listarJustificacions() {
        return controlador.findJustificacionEntities();
    }
    
}
