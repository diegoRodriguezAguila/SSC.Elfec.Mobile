package com.elfec.ssc.model;

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
public class Account {

    @SerializedName("AccountNumber")
    private String accountNumber;

    @SerializedName("NUS")
    private String nus;

    @SerializedName("AccountOwner")
    private String accountOwner;

    @SerializedName("Address")
    private String address;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("EnergySupplyStatus")
    private short energySupplyStatus;

    @SerializedName("Status")
    private short status;

    @SerializedName("created_at")
    private DateTime insertDate;

    @SerializedName("updated_at")
    private DateTime updateDate;

    @SerializedName("Debts")
    private List<Debt> debts;

    public Account() {
        super();
    }

    public Account(String nus) {
        this.nus = nus;
    }

    public Account(String accountNumber, String nus) {
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
