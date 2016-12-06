package com.elfec.ssc.model;

import com.elfec.ssc.model.enums.ClientStatus;

import org.joda.time.DateTime;

/**
 * Guarda la información del cliente que utiliza la aplicación
 *
 * @author Diego
 */
public class Client{

    private String gmail;
    /**
     * Los diferentes estados de los clientes segun {@link ClientStatus} representados en enteros
     */
    private short status;
    private DateTime insertDate;
    private DateTime updateDate;

    public Client() {
    }

    public Client(String gmail, ClientStatus status) {
        this.gmail = gmail;
        setStatus(status);
    }

    //region Getters y Setters

    public String getGmail() {
        return gmail;
    }


    public void setGmail(String gmail) {
        this.gmail = gmail;
    }


    public ClientStatus getStatus() {
        return ClientStatus.get(status);
    }


    public void setStatus(ClientStatus status) {
        this.status = status.toShort();
    }


    public DateTime getInsertDate() {
        return insertDate;
    }


    public void setInsertDate(DateTime insertDate) {
        this.insertDate = insertDate;
    }


    public DateTime getUpdateDate() {
        return updateDate;
    }


    public void setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
    }

    //endregion

}
