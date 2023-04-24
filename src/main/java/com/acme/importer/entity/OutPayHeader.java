package com.acme.importer.entity;

import java.sql.Date;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "out_pay_header")
public class OutPayHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Outpay_Header_ID;

    @Nonnull
    private String clntnum;
    
    @Nonnull
    private String chdrnum;
    
    @Nonnull
    private String letterType;
    
    @Nonnull
    private Date printDate;
    
    private String dataID;
    private String clntName;
    private String clntAddress;
    private Date regDate;
    private Double benPercent;
    private String role1;
    private String role2;
    private String cownNum;
    private String cownName;
    private String notice01;
    private String notice02;
    private String notice03;
    private String notice04;
    private String notice05;
    private String notice06;
    private String claimId;
    private Date tp2ProcessDate;

    public String getClntnum() {
        return clntnum;
    }

    public void setClntnum(String clntnum) {
        this.clntnum = clntnum;
    }

    public String getChdrnum() {
        return chdrnum;
    }

    public void setChdrnum(String chdrnum) {
        this.chdrnum = chdrnum;
    }

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public String getDataID() {
        return dataID;
    }

    public void setDataID(String dataID) {
        this.dataID = dataID;
    }

    public String getClntName() {
        return clntName;
    }

    public void setClntName(String clntName) {
        this.clntName = clntName;
    }

    public String getClntAddress() {
        return clntAddress;
    }

    public void setClntAddress(String clntAddress) {
        this.clntAddress = clntAddress;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Double getBenPercent() {
        return benPercent;
    }

    public void setBenPercent(Double benPercent) {
        this.benPercent = benPercent;
    }

    public String getRole1() {
        return role1;
    }

    public void setRole1(String role1) {
        this.role1 = role1;
    }

    public String getRole2() {
        return role2;
    }

    public void setRole2(String role2) {
        this.role2 = role2;
    }

    public String getCownNum() {
        return cownNum;
    }

    public void setCownNum(String cownNum) {
        this.cownNum = cownNum;
    }

    public String getCownName() {
        return cownName;
    }

    public void setCownName(String cownName) {
        this.cownName = cownName;
    }

    public String getNotice01() {
        return notice01;
    }

    public void setNotice01(String notice01) {
        this.notice01 = notice01;
    }

    public String getNotice02() {
        return notice02;
    }

    public void setNotice02(String notice02) {
        this.notice02 = notice02;
    }

    public String getNotice03() {
        return notice03;
    }

    public void setNotice03(String notice03) {
        this.notice03 = notice03;
    }

    public String getNotice04() {
        return notice04;
    }

    public void setNotice04(String notice04) {
        this.notice04 = notice04;
    }

    public String getNotice05() {
        return notice05;
    }

    public void setNotice05(String notice05) {
        this.notice05 = notice05;
    }

    public String getNotice06() {
        return notice06;
    }

    public void setNotice06(String notice06) {
        this.notice06 = notice06;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public Date getTp2ProcessDate() {
        return tp2ProcessDate;
    }

    public void setTp2ProcessDate(Date tp2ProcessDate) {
        this.tp2ProcessDate = tp2ProcessDate;
    }

    @Override
    public String toString() {
        return "OutpayHeader [clntnum=" + clntnum + ", chdrnum=" + chdrnum + ", letterType=" + letterType
                + ", printDate=" + printDate + ", dataID=" + dataID + ", clntName=" + clntName + ", clntAddress="
                + clntAddress + ", regDate=" + regDate + ", benPercent=" + benPercent + ", role1=" + role1 + ", role2="
                + role2 + ", cownNum=" + cownNum + ", cownName=" + cownName + ", notice01=" + notice01 + ", notice02="
                + notice02 + ", notice03=" + notice03 + ", notice04=" + notice04 + ", notice05=" + notice05
                + ", notice06=" + notice06 + ", claim_ID=" + claimId + ", tP2ProcessDate=" + tp2ProcessDate + "]";
    }
}
