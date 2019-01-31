/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iadb.ws.timeandlabor.model.sf;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

/**
 *
 * @author DesarrolloPc
 */
@Entity
@SqlResultSetMapping(name = "defaultTmpItemsSFDTO",
        entities = @EntityResult(entityClass = TmpItemsSFDto.class))
public class TmpItemsSFDto implements Serializable {

    @Id
    private Integer rid;
    private String userId;
    private String attendanceType;
    private String dateEnd;
    private String dateCompletion;
    private Double hoursTotal;
    private String wbs;
    private String fund;

    public TmpItemsSFDto() {
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(String dateComplation) {
        this.dateCompletion = dateComplation;
    }

    public Double getHoursTotal() {
        return hoursTotal;
    }

    public void setHoursTotal(Double hoursTotal) {
        this.hoursTotal = hoursTotal;
    }

    public String getWbs() {
        return wbs;
    }

    public void setWbs(String wbs) {
        this.wbs = wbs;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }
}
