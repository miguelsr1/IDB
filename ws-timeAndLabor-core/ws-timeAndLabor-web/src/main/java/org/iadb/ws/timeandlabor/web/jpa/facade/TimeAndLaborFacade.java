/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iadb.ws.timeandlabor.web.jpa.facade;

import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.iadb.ws.timeandlabor.model.tal.TblProcesoExp;

/**
 *
 * @author DesarrolloPc
 */
@Stateless
@LocalBean
public class TimeAndLaborFacade {

    @PersistenceContext(unitName = "timeAndLabor-PU")
    public EntityManager emTal;
    
    @TransactionAttribute(REQUIRES_NEW)
    public void registrarProceso(Date fechaInicio, Date fechaFin, Integer numeroRegistros){
        TblProcesoExp procesoExp = new TblProcesoExp();
        procesoExp.setFechaExportacion(fechaInicio);
        procesoExp.setHoraInicio(fechaInicio);
        procesoExp.setHoraFin(fechaFin);
        procesoExp.setNumeroRegistros(numeroRegistros);
        
        emTal.persist(procesoExp);
    }
}
