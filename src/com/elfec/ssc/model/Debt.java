package com.elfec.ssc.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

/**
 * Abstracción de las deudas del usuario
 * @author drodriguez
 *
 */
@Table(name = "Debts")
public class Debt extends Model {

	@Column(name = "Account", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Account Account;
	
	@Column(name = "Amount", notNull=true)
	private BigDecimal Amount;
	
	@Column(name = "Year", notNull=true)
    private int Year;
	
	@Column(name = "Month", notNull=true)
    private short Month;
	
	@Column(name = "ReceiptNumber")
    private int ReceiptNumber;
	
	@Column(name = "ExpirationDate")
    private DateTime ExpirationDate;
	
	@Column(name = "Status", notNull=true)
    private short Status;
    
    @Column(name = "InsertDate", notNull=true)
    private DateTime InsertDate;
    
    @Column(name = "UpdateDate")
    private DateTime UpdateDate;
    
    public Debt() {
		super();
	}

	public Debt(Account fromAccount, BigDecimal amount, int year,
			short month, int receiptNumber, DateTime expirationDate) {
		super();
		this.Account = fromAccount;
		this.Amount = amount.setScale(2, RoundingMode.CEILING);
		this.Year = year;
		this.Month = month;
		this.ReceiptNumber = receiptNumber;
		this.ExpirationDate = expirationDate;
		this.Status = 1;
	}
	
	
	//#region Getters y Setters
	

	public Account getAccount() {
		return Account;
	}

	public void setAccount(Account account) {
		Account = account;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount.setScale(2, RoundingMode.CEILING);
	}

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
		Year = year;
	}

	public short getMonth() {
		return Month;
	}

	public void setMonth(short month) {
		Month = month;
	}

	public int getReceiptNumber() {
		return ReceiptNumber;
	}

	public void setReceiptNumber(int receiptNumber) {
		ReceiptNumber = receiptNumber;
	}

	public DateTime getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(DateTime expirationDate) {
		ExpirationDate = expirationDate;
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
