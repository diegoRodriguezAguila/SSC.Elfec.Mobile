package com.elfec.ssc.model;

import com.google.gson.annotations.SerializedName;

public class Usage {

    @SerializedName("EnergyUsage")
    private int energyUsage;

    @SerializedName("Term")
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
