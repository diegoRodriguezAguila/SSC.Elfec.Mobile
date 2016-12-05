package com.elfec.ssc.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.elfec.ssc.helpers.utils.ObjectsCompat;
import com.elfec.ssc.model.enums.AccountEnergySupplyStatus;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * Abstracción de las cuentas de usuario
 *
 * @author Diego
 */
@Table(name = "Accounts")
public class Account {

    @Column(name = "Client")
    private Client client;

    @SerializedName("AccountNumber")
    @Column(name = "AccountNumber")
    private String accountNumber;

    @SerializedName("NUS")
    @Column(name = "NUS", index = true, notNull = true)
    private String nus;

    @SerializedName("AccountOwner")
    @Column(name = "AccountOwner")
    private String accountOwner;

    @SerializedName("Address")
    @Column(name = "Address")
    private String address;

    @SerializedName("Longitude")
    @Column(name = "Longitude")
    private double longitude;

    @SerializedName("Latitude")
    @Column(name = "Latitude")
    private double latitude;

    @SerializedName("EnergySupplyStatus")
    @Column(name = "EnergySupplyStatus")
    private short energySupplyStatus;

    @SerializedName("Status")
    @Column(name = "Status", notNull = true)
    private short status;

    @SerializedName("created_at")
    @Column(name = "InsertDate", notNull = true)
    private DateTime insertDate;

    @SerializedName("updated_at")
    @Column(name = "UpdateDate")
    private DateTime updateDate;

    @SerializedName("Debts")
    private List<Debt> debts;

    public Account() {
        super();
    }

    public Account(Client ownerClient, String accountNumber, String nus) {
        super();
        this.client = ownerClient;
        this.accountNumber = accountNumber;
        this.nus = nus;
        this.status = 1;
    }

    /**
     * Copia los atributos de la cuenta del parámetro a la cuenta actual. Los
     * atributos que se copian son:
     * <ul>
     * <li>AccountOwner</li>
     * <li>AccountNumber</li>
     * <li>Address</li>
     * <li>Latitude</li>
     * <li>Longitude</li>
     * <li>EnergySupplyStatus</li>
     * <li>Debts</li>
     * </ul>
     * No guarda los cambios por tanto tiene que llamarse a save() para que persistan
     *
     * @param account cuenta
     */
    public void copyAttributes(Account account) {
        this.accountOwner = account.getAccountOwner();
        this.accountNumber = account.getAccountNumber();
        this.address = account.getAddress();
        this.latitude = account.getLatitude();
        this.longitude = account.getLongitude();
        setEnergySupplyStatus(account.getEnergySupplyStatus());
        this.debts = account.getDebts();
    }

    public BigDecimal getTotalDebt() {
        BigDecimal total = BigDecimal.ZERO;
        for (Debt debt : getDebts()) {
            total = total.add(debt.getAmount());
        }
        return total;
    }

    //region Getters y Setters

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNus() {
        return nus;
    }

    public void setNus(String nUS) {
        nus = nUS;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public AccountEnergySupplyStatus getEnergySupplyStatus() {
        return AccountEnergySupplyStatus.get(energySupplyStatus);
    }

    public void setEnergySupplyStatus(
            AccountEnergySupplyStatus energySupplyStatus) {
        this.energySupplyStatus = energySupplyStatus.toShort();
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
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

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    //endregion

    /**
     * Busca una cuenta que coincida con los parámetros
     *
     * @param gmail
     * @param nus
     * @return
     */
    public static Account findAccount(String gmail, String nus) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ObjectsCompat.equals(nus, account.nus);

    }

    @Override
    public int hashCode() {
        if (nus == null)
            return super.hashCode();
        return nus.hashCode();
    }
}
