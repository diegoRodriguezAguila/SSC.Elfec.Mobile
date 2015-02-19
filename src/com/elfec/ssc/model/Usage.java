package com.elfec.ssc.model;

import com.activeandroid.annotation.Column;

public class Usage {
	@Column(name = "EnergyUsage")
	private String EnergyUsage;
	@Column(name = "Term")
    private String Term;
	
	public Usage()
	{
		
	}
	public Usage(String EnergyUsage,String Term)
	{
		this.EnergyUsage=EnergyUsage;
		this.Term=Term;
	}
	public String getEnergyUsage() {
		return EnergyUsage;
	}
	public void setEnergyUsage(String energyUsage) {
		EnergyUsage = energyUsage;
	}
	public String getTerm() {
		return Term;
	}
	public void setTerm(String term) {
		Term = term;
	}
	
}
