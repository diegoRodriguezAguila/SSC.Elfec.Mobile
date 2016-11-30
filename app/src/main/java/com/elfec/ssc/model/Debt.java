package com.elfec.ssc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstracción de las deudas del usuario
 *
 * @author drodriguez
 */
@Table(name = "Debts")
public class Debt extends Model implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4328967895169344533L;

    @Column(name = "Account", notNull = true, onDelete = ForeignKeyAction.CASCADE)
    private Account account;

    @SerializedName("Amount")
    @Column(name = "Amount", notNull = true)
    private BigDecimal amount = BigDecimal.ZERO;

    @SerializedName("Year")
    @Column(name = "Year", notNull = true)
    private int year;

    @SerializedName("Month")
    @Column(name = "Month", notNull = true)
    private short month;

    @SerializedName("ReceiptNumber")
    @Column(name = "ReceiptNumber")
    private int receiptNumber;

    @SerializedName("ExpirationDate")
    @Column(name = "ExpirationDate")
    private DateTime expirationDate;

    @SerializedName("Status")
    @Column(name = "Status", notNull = true)
    private short status;

    @SerializedName("created_at")
    @Column(name = "InsertDate", notNull = true)
    private DateTime insertDate;

    @SerializedName("updated_at")
    @Column(name = "UpdateDate")
    private DateTime updateDate;

    public Debt() {
        super();
    }

    public Debt(Account fromAccount, BigDecimal amount, int year,
                short month, int receiptNumber, DateTime expirationDate) {
        super();
        this.account = fromAccount;
        this.amount = amount.setScale(2, RoundingMode.CEILING);
        this.year = year;
        this.month = month;
        this.receiptNumber = receiptNumber;
        this.expirationDate = expirationDate;
        this.status = 1;
    }


    //region Getters y Setters


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.CEILING);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
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
    //endregion


}
