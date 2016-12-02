package com.elfec.ssc.model;

import com.elfec.ssc.model.enums.ClientStatus;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda la información del cliente que utiliza la aplicación
 *
 * @author Diego
 */
public class Client implements Serializable {

    private String gmail;
    /**
     * Los diferentes estados de los clientes segun {@link ClientStatus} representados en enteros
     */
    private short status;
    private DateTime insertDate;
    private DateTime updateDate;

    public Client() {
        super();
    }

    public Client(String gmail, ClientStatus status) {
        super();
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

    /**
     * Obtiene todas las cuentas relacionadas al cliente
     *
     * @return Lista de cuentas relacionadas
     */
    public List<Account> getAccounts() {
        return new ArrayList<>();
    }

    /**
     * Obtiene el las cuentas activas de un cliente
     *
     * @return el cuentas activas de un cliente
     */
    public List<Account> getActiveAccounts() {
        return new ArrayList<>();

    }

    /**
     * Retorna true si es que el usuario tiene una cuenta con el nus y numero provistos, y en estado activo = 1
     *
     * @param nus
     * @param accountNumber
     * @return
     */
    public boolean hasAccount(String nus, String accountNumber) {
        return false;
    }

}
