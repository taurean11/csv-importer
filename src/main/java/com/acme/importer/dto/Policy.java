package com.acme.importer.dto;

public class Policy {

    private String chdrnum;
    private String cownnum;
    private String ownerName;
    private String lifcNum;
    private String lifcName;
    private String aracde;
    private String agntnum;
    private String mailAddress;

    public String getChdrnum() {
        return chdrnum;
    }

    public void setChdrnum(String chdrnum) {
        this.chdrnum = chdrnum;
    }

    public String getCownnum() {
        return cownnum;
    }

    public void setCownnum(String cownnum) {
        this.cownnum = cownnum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLifcNum() {
        return lifcNum;
    }

    public void setLifcNum(String lifcNum) {
        this.lifcNum = lifcNum;
    }

    public String getLifcName() {
        return lifcName;
    }

    public void setLifcName(String lifcName) {
        this.lifcName = lifcName;
    }

    public String getAracde() {
        return aracde;
    }

    public void setAracde(String aracde) {
        this.aracde = aracde;
    }

    public String getAgntnum() {
        return agntnum;
    }

    public void setAgntnum(String agntnum) {
        this.agntnum = agntnum;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    @Override
    public String toString() {
        return "Policy [chdrnum=" + chdrnum + ", cownnum=" + cownnum + ", ownerName=" + ownerName + ", lifcNum="
                + lifcNum + ", lifcName=" + lifcName + ", aracde=" + aracde + ", agntnum=" + agntnum + ", mailAddress="
                + mailAddress + "]";
    }
}
