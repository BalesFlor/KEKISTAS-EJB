/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.grsc.logica.ejb;

import com.grsc.modelo.entities.Analista;
import java.math.BigInteger;
import javax.ejb.Remote;

/**
 *
 * @author fbale
 */
@Remote
public interface AnalistaBeanRemote {
    Boolean existeAnalista(BigInteger idUser);
    Boolean ingresarAnalista(BigInteger id);
    Boolean eliminarAnalista(BigInteger idUser);
    Analista buscarAnalista(BigInteger id);
}
