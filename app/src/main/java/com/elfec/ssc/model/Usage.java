package com.elfec.ssc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.google.gson.annotations.SerializedName;

public class Usage extends Model {

    @Column(name = "Account", notNull = true, onDelete = ForeignKeyAction.CASCADE)
    private Account account;

    @SerializedName("EnergyUsage")
    @Column(name = "EnergyUsage")
    private int energyUsage;

    @SerializedName("Term")
    @Column(name = "Term")
    private String term;

    public Usage() {
        super();
    }

    public Usage(int energyUsage, String term) {
        super();
        this.energyUsage = energyUsage;
        this.term = term;
    }

    //region getter setters

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(int energyUsage) {
        this.energyUsage = energyUsage;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    //endregion

}
