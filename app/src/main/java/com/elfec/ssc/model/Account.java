package com.elfec.ssc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstracción de las cuentas de usuario
 * 
 * @author Diego
 *
 */
@Table(name = "Accounts")
public class Account extends Model {

	public Account() {
		super();
	}

	public Account(Client ownerClient, String accountNumber, String nus) {
		super();
		this.Client = ownerClient;
		this.AccountNumber = accountNumber;
		this.NUS = nus;
		this.Status = 1;
	}

	public Account(Client client, String accountNumber, String nUS,
			String accountOwner, String address, double latitude,
			double longitude, short energySupplyStatus) {
		super();
		this.Client = client;
		this.AccountNumber = accountNumber;
		this.NUS = nUS;
		this.AccountOwner = accountOwner;
		this.Address = address;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.EnergySupplyStatus = energySupplyStatus;
		this.Status = 1;
	}

	@Column(name = "Client")
	private Client Client;

	@Column(name = "AccountNumber")
	private String AccountNumber;

	@Column(name = "NUS", index = true, notNull = true)
	private String NUS;

	@Column(name = "AccountOwner")
	private String AccountOwner;

	@Column(name = "Address")
	private String Address;

	@Column(name = "Longitude")
	private double Longitude;

	@Column(name = "Latitude")
	private double Latitude;

	@Column(name = "EnergySupplyStatus")
	private short EnergySupplyStatus;

	@Column(name = "Status", notNull = true)
	private short Status;

	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;

	@Column(name = "UpdateDate")
	private DateTime UpdateDate;

	private List<Debt> Debts;

	private List<Usage> Usages;

	/**
	 * Copia los atributos de la cuenta del parámetro a la cuenta actual. Los
	 * atributos que se copian son:
	 * <ul>
	 *     <li>AccountOwner</li>
	 *     <li>AccountNumber</li>
	 *     <li>Address</li>
	 *     <li>Latitude</li>
	 *     <li>Longitude</li>
	 *     <li>EnergySupplyStatus</li>
     *     <li>Debts</li>
	 * </ul>
     * No guarda los cambios por tanto tiene que llamarse a save() para que persistan
	 * @param account cuenta
	 */
	public void copyAttributes(Account account){
		this.AccountOwner = account.getAccountOwner();
		this.AccountNumber = account.getAccountNumber();
		this.Address = account.getAddress();
		this.Latitude = account.getLatitude();
		this.Longitude = account.getLongitude();
		setEnergySupplyStatus(account.getEnergySupplyStatus());
        this.addDebts(account.getDebts());
	}

	public BigDecimal getTotalDebt() {
		BigDecimal total = BigDecimal.ZERO;
		for (Debt debt : getDebts()) {
			total = total.add(debt.getAmount());
		}
		return total;
	}

	// #region Getters y Setters

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

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public AccountEnergySupplyStatus getEnergySupplyStatus() {
		return AccountEnergySupplyStatus.get(EnergySupplyStatus);
	}

	public void setEnergySupplyStatus(
			AccountEnergySupplyStatus energySupplyStatus) {
		EnergySupplyStatus = energySupplyStatus.toShort();
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

	// #endregion

	/**
	 * Obtiene todas las deudas relacionadas a la cuenta
	 * 
	 * @return Lista de deudas relacionadas
	 */
	public List<Debt> getDebts() {
		if (Debts == null) {
			try {
				Debts = getMany(Debt.class, "Account");
			} catch (NullPointerException e) {
				Debts = new ArrayList<>();
			}
		}
		return Debts;
	}

	/**
	 * Obtiene los consumos relacionados a la cuenta
	 * 
	 * @return Lista de consumos relacionadas
	 */
	public List<Usage> getUsages() {
		if (Usages == null) {
			try {
				Usages = getMany(Usage.class, "Account");
			} catch (NullPointerException e) {
				Usages = new ArrayList<>();
			}
		}
		return Usages;
	}

	/**
	 * Elimina todos los consumos relacionados a la cuenta
	 */
	public void removeUsages() {
		Usages = null;
		new Delete().from(Usage.class).where("Account=?", getId()).execute();
	}

	public String getAccountOwner() {
		return AccountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		AccountOwner = accountOwner;
	}

	/**
	 * Elimina todas las deudas de esta cuenta
	 */
	public void removeAllDebts() {
        Debts = null;
        new Delete().from(Debt.class).where("Account=?", getId()).execute();
	}

	/**
	 * Agrega una lista de deudas siempre
	 * 
	 * @param newDebt
	 */
	public void addDebts(List<Debt> newDebt) {
		List<Debt> debts = getDebts();
		debts.addAll(newDebt);
	}

	/**
	 * Busca una cuenta que coincida con los parámetros
	 * 
	 * @param gmail
	 * @param nus
	 * @return
	 */
	public static Account findAccount(String gmail, String nus) {
		return new Select().from(Account.class).as("a").join(Client.class)
				.as("c").on("a.Client=c.Id")
				.where("NUS=? AND Gmail=?", nus, gmail).executeSingle();
	}


	/**
	 * Busca una cuenta con el id proporcionado
	 * 
	 * @param id
	 * @return cuenta con el id proporcionado
	 */
	public static Account get(Long id) {
		return new Select().from(Account.class).where("Id = ?", id)
				.executeSingle();
	}
}
