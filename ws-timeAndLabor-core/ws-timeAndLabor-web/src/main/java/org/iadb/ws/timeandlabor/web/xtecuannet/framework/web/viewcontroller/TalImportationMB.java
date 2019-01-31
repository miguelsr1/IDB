/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.web.viewcontroller;


import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import org.iadb.ws.timeandlabor.web.jpa.facade.MailSender;
import org.iadb.ws.timeandlabor.web.jpa.facade.SendDataFacade;
import org.iadb.ws.timeandlabor.web.jpa.facade.TimeAndLaborFacade;

/**
 *
 * @author DesarrolloPc
 */
@ManagedBean(name = "talImportationMB")
@ViewScoped
public class TalImportationMB implements Serializable{

    @EJB
    private SendDataFacade sendDataFacade;
    @EJB
    private TimeAndLaborFacade timeAndLaborFacade;
    @EJB
    private MailSender mailSender;
    
    /**
     * Creates a new instance of TalImportationMB
     */
    public TalImportationMB() {
    }
    
    public void actionImportation(){
        Integer numRegistros;
        Date fechaInicio = new Date();
        Date fechaFin;
        Map<String, Object> result;

        numRegistros = sendDataFacade.sendDataToSAP();
        fechaFin = new Date();

        timeAndLaborFacade.registrarProceso(fechaInicio, fechaFin, numRegistros);

        result = new HashMap<>();
        result.put("numRegistros", numRegistros);
        
        mailSender.sendConfirmationExportTimeAndLabor(result);
    }
}
