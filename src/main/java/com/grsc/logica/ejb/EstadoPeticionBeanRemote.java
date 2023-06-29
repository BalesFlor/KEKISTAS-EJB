/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grsc.logica.ejb;

import com.grsc.modelo.entities.EstadoPeticion;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author fbale
 */
public interface EstadoPeticionBeanRemote {
    List<EstadoPeticion> listarEstados();
    EstadoPeticion buscar(BigInteger id);
}
