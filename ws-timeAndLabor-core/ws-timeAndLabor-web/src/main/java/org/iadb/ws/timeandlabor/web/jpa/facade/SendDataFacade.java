/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iadb.ws.timeandlabor.web.jpa.facade;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import org.iadb.ws.timeandlabor.model.sf.TmpItemsSFDto;
import ws.tal.test.BizTalkServiceInstance;
import ws.tal.test.TimeAndLabor;
import ws.tal.test.WcfServiceTimeAndLaborInterface;
/**
 *
 * @author martin
 */
@Singleton
@LocalBean
public class SendDataFacade {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/awtabts01.idb.iadb.org/TimeAndLaborEntry/WcfService_TimeAndLaborInterface.svc.singlewsdl.wsdl")
    private BizTalkServiceInstance service;

    private final SimpleDateFormat sdfEnd = new SimpleDateFormat("MM/dd/yyyy");

    @PersistenceContext(unitName = "successFactor-PU")
    public EntityManager emSF;

//    @Schedule(minute = "*/1", hour = "*", info = "Every 1 minutos")
//    public void prueba() {
//        TblProcesoExp tbl = new TblProcesoExp();
//        tbl.setFechaExportacion(new Date());
//        tbl.setNumeroRegistros(100);
//
//        emTal.persist(tbl);
//
//        System.out.println("insert");
//    }
//    @Schedule(minute = "*/1", hour = "*", info = "Every 1 minutos")
//    public void prueba() {
//        System.out.println("insert");
//    }

    public Integer sendDataToSAP() {

        WcfServiceTimeAndLaborInterface port = service.getWSHttpBindingITwoWayAsync();
        TimeAndLabor part = new TimeAndLabor(); //Objeto a enviar al WS
        Double totalHoras;
        Double numHorasAcumuladas;

//        Integer monthComplation; //mes en el que se registra el complation
//        Integer monthEnd; //mes en el que finaliza el curso
        Integer monthCurrent; //mes actual
        Integer totalDeEntradas = 0;

        String numHoras; //numero de horas por entrada a reportar

//        Date dateEndItem;
//        Date dateComplation;
        Date dateNow = new Date();

        Query query = emSF.createNamedQuery("ItemsReportsSF.itemsReports", TmpItemsSFDto.class);
        List<TmpItemsSFDto> lstItemsReportsDTOs = query.getResultList();

        //Recorrido de todos los item's a reportar para la creación de entradas
        for (TmpItemsSFDto item : lstItemsReportsDTOs) {
            totalDeEntradas += 1;

            numHorasAcumuladas = 0d;

            totalHoras = item.getHoursTotal();

            //dateEndItem = sdfEnd.parse(item.getDateEnd());
            //monthEnd = calendarUtil.getMonthFromDate(dateEndItem);
            //dateComplation = sdfComplation.parse(item.getDateComplation().replace(" America/New York", ""));
            //monthComplation = calendarUtil.getMonthFromDate(dateComplation);
            monthCurrent = getMonthFromDate(dateNow);

            numHoras = String.valueOf((int) (totalHoras / 8));

            Double tmp = 0d;
            int count = 0;
            if (numHoras.equals("0")) {
                numHoras = String.valueOf(totalHoras);
                count++;
            } else {
                do {
                    tmp += Double.parseDouble(numHoras);
                    count++;
                } while (tmp <= totalHoras);
            }

            if ((part.getEntry().size() + count) > 100) {
                //hacer envio de los primeros 100
                //port.insertTimeAndLaborEntry(part);
                System.out.println("Envio de datos al WS, tamaño del arreglo = " + part.getEntry().size());

                //Inicialización de nuevo set de datos a exportar
                part = new TimeAndLabor();
            }

            do {
                if (new Double((numHorasAcumuladas + Double.parseDouble(numHoras))) > totalHoras) {
                    numHoras = String.valueOf(totalHoras - numHorasAcumuladas);
                }
                numHorasAcumuladas += Double.parseDouble(numHoras);

                TimeAndLabor.Entry ent = new TimeAndLabor.Entry();
                ent.setEmployeeNumber(item.getUserId());
                ent.setAttendanceType(item.getAttendanceType());
                ent.setDate(sdfEnd.format(dateNow));
                ent.setHours(numHoras);
                ent.setWBS(item.getWbs());
                ent.setFund(item.getFund());

                part.getEntry().add(ent);

                dateNow = addOneDayToDate(dateNow, monthCurrent);
            } while (!Objects.equals(numHorasAcumuladas, totalHoras));
        }

        return totalDeEntradas;
    }

    /**
     * Agrega un dia a la fecha que recibe como parametro. Si el dia sobrepasa
     * el mes de la fecha, dicha fecha se reinicia al primer día del mes.
     *
     * @param date fecha a sumar un día
     * @param currentMonth mes actual
     * @return
     */
    private Date addOneDayToDate(Date date, int currentMonth) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);

        //verificar que la suma anterior no sobrepasa el mes actual
        if (getMonthFromDate(date) != getMonthFromDate(new Date())) {
            //inicializar al mes actual y el primer día de dicho mes
            c.set(Calendar.MONTH, currentMonth);
            c.set(Calendar.DAY_OF_MONTH, 1);
        }

        return c.getTime();
    }

    /**
     * Meses numerados desde 1 al 12
     *
     * @param date
     * @return
     */
    private int getMonthFromDate(Date date) {
        LocalDate localTmpDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localTmpDate.getMonthValue();
    }
}
