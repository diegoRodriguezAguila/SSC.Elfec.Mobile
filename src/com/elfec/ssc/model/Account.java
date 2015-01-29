package com.elfec.ssc.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Abstracción de las cuentas de usuario
 * @author Diego
 *
 */
@Table(name = "Accounts")
public class Account extends Model {

	public Account() {
		super();
	}
	
	public Account(Client ownerClient, String accountNumber, String nus)
	{
		super();
		this.Client = ownerClient;
		this.AccountNumber = accountNumber;
		this.NUS = nus;
		this.Status = 1;
	}
	
	
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

	
	/**
	 * Busca una cuenta que coincida con los parámetros
	 * @param gmail
	 * @param nus
	 * @return
	 */
	public static Account findAccount(String gmail,String nus)
	{
		return new Select()
        .from(Account.class).as("a").join(Client.class).as("c").on("a.Client=c.Id").where("NUS=? AND Gmail=?", nus,gmail)
        .executeSingle();
	}
	
	/**
	 * Busca una cuenta que coincida con los parámetros
	 * @param gmail
	 * @param nus
	 * @param accountNumber
	 * @return
	 */
	public static Account findAccount(String gmail,String nus, String accountNumber)
	{
		return new Select()
        .from(Account.class).as("a").join(Client.class).as("c").on("a.Client=c.Id").where("NUS=? AND AccountNumber=? AND Gmail=?", nus, accountNumber, gmail)
        .executeSingle();
	}
}
