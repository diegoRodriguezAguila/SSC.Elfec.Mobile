package com.elfec.ssc.model;

import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.elfec.ssc.model.enums.ClientStatus;

/**
 * Guarda la información del cliente que utiliza la aplicación
 * @author Diego
 */
@Table(name = "Clients")
public class Client extends Model {
	
	public Client() {
		super();
	}
	
	public Client(String gmail, ClientStatus status) {
		super();
		this.Gmail = gmail;
		setStatus(status);
	}

	@Column(name = "Gmail")
	private String Gmail;

	/**
	 * Los diferentes estados de los clientes segun {@link ClientStatus} representados en enteros
	 */
	@Column(name = "Status", notNull = true)
	private short Status;

	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;

	@Column(name = "UpdateDate")
	private DateTime UpdateDate;
	
	//#region Getters y Setters
	
	public String getGmail() {
		return Gmail;
	}


	public void setGmail(String gmail) {
		Gmail = gmail;
	}


	public ClientStatus getStatus() {
		return ClientStatus.get(Status);
	}


	public void setStatus(ClientStatus status) {
		Status = status.toShort();
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
	 * Obtiene todas las cuentas relacionadas al cliente
	 * @return Lista de cuentas relacionadas
	 */
	public List<Account> getAccounts() 
	{
		return getMany(Account.class, "Client");
	}
	
	/**
	 * Obtiene el cliente que tenga estado activo
	 * @return el cliente activo, null si es que no se registró ningun cliente aún
	 */
	public static Client getActiveClient()
	{
		return new Select()
        .from(Client.class).where("Status=?", ClientStatus.ACTIVE.toShort())
        .executeSingle();
		
	}
	/**
	 * retorna un cliente en base a una direccion de correo electronico
	 * @param gmail
	 * @return Client
	 */
	public static Client getClientByGmail(String gmail)
	{
		return new Select()
        .from(Client.class).where("Status=? AND Gmail=?", ClientStatus.ACTIVE.toShort(),gmail)
        .executeSingle();
	}
	/**
	 * Obtiene el las cuentas activas de un cliente
	 * @return el cuentas activas de un cliente
	 */
	public List<Account> getActiveAccounts()
	{
		return  new Select()
        .from(Account.class).as("a").join(Client.class).as("c").on("a.Client=c.Id").where("a.Status=1 AND c.Gmail=?",this.Gmail)
        .orderBy("a.AccountNumber ASC")
        .execute();
		
	}
	
	/**
	 * Retorna true si es que el usuario tiene una cuenta con el nus y numero provistos, y en estado activo = 1
	 * @param nus
	 * @param accountNumber
	 * @return
	 */
	public boolean hasAccount(String nus, String accountNumber)
	{
		return  new Select()
        .from(Account.class).as("a").join(Client.class).as("c").on("a.Client=c.Id").where("a.Status=1 AND a.NUS=? AND a.AccountNumber=? AND c.Gmail=?",
        		nus, accountNumber, this.Gmail)
        .executeSingle()!=null;
	}
	
}
