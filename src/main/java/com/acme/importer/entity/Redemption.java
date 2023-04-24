package com.acme.importer.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SurValues")
public class Redemption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Nonnull
    private String chdrnum;

    @Nonnull
    private Double survalue;

    @Nonnull
    private String company;

    private String currency;
    private String validDate;

    public String getChdrnum() {
        return chdrnum;
    }

    public void setChdrnum(String chdrnum) {
        this.chdrnum = chdrnum;
    }

    public Double getSurvalue() {
        return survalue;
    }

    public void setSurvalue(Double survalue) {
        this.survalue = survalue;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    @Override
    public String toString() {
        return "Redemption [chdrnum=" + chdrnum + ", survalue=" + survalue + ", company=" + company + ", currency="
                + currency + ", validDate=" + validDate + "]";
    }
}
