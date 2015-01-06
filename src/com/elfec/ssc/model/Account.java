package com.elfec.ssc.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Abstracción de las cuentas de usuario
 * @author Diego
 *
 */
@Table(name = "Accounts")
public class Account extends Model {

	@Column(name = "Client")
    private Client Client;
    
    @Column(name = "AccountNumber")
    private String AccountNumber;
    
    @Column(name = "NUS", index=true, notNull=true)
    private String NUS;
    
    @Column(name = "Status", notNull=true)
    private short Status;
    
    @Column(name = "InsertDate", notNull=true)
    private DateTime InsertDate;
    
    @Column(name = "UpdateDate")
    private DateTime UpdateDate;
    
	//#region Getters y Setters

	public Client getClient() {
		return Client;
	}
	public void setClient(Client client) {
		Client = client;
	}
	
	public String getAccountNumber() {
		return AccountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}
	
	public String getNUS() {
		return NUS;
	}
	public void setNUS(String nUS) {
		NUS = nUS;
	}
	
	public short getStatus() {
		return Status;
	}
	public void setStatus(short status) {
		Status = status;
	}
	
	public DateTime getInsertDate() {
		return InsertDate;
	}
	public void setInsertDate(DateTime insertDate) {
		InsertDate = insertDate;
	}
	
	public DateTime getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(DateTime updateDate) {
		UpdateDate = updateDate;
	}
    
    //#endregion

}
