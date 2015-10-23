package com.elfec.ssc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;

public class Usage extends Model{
	
	@Column(name = "Account", notNull=true, onDelete=ForeignKeyAction.CASCADE)
	private Account Account;
	@Column(name = "EnergyUsage")
	private int EnergyUsage;
	@Column(name = "Term")
    private String Term;
	
	public Usage()
	{
		
	}
	
	public Usage(int EnergyUsage, String Term)
	{
		this.EnergyUsage=EnergyUsage;
		this.Term=Term;
	}
	
	public Account getAccount() {
		return Account;
	}

	public void setAccount(Account account) {
		Account = account;
	}

	public int getEnergyUsage() {
		return EnergyUsage;
	}
	public void setEnergyUsage(int energyUsage) {
		EnergyUsage = energyUsage;
	}
	public String getTerm() {
		return Term;
	}
	public void setTerm(String term) {
		Term = term;
	}
	
}
