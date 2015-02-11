package com.elfec.ssc.model;

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
	private String Amount;
	
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
}
