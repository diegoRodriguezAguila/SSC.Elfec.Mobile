package com.elfec.ssc.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstracción de las deudas del usuario
 *
 * @author drodriguez
 */
public class Debt {

    @SerializedName("Amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @SerializedName("Year")
    private int year;

    @SerializedName("Month")
    private short month;

    @SerializedName("ReceiptNumber")
    private int receiptNumber;

    @SerializedName("ExpirationDate")
    private DateTime expirationDate;

    @SerializedName("Status")
    private short status;

    @SerializedName("created_at")
    private DateTime insertDate;

    @SerializedName("updated_at")
    private DateTime updateDate;

    public Debt() {
    }

    public Debt(BigDecimal amount, int year,
                short month, int receiptNumber, DateTime expirationDate) {
        this.amount = amount.setScale(2, RoundingMode.CEILING);
        this.year = year;
        this.month = month;
        this.receiptNumber = receiptNumber;
        this.expirationDate = expirationDate;
        this.status = 1;
    }


    //region Getters y Setters

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
