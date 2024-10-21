package org.pavani.multithreading.trading_multithreading.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "positions")
@Data
public class Positions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private int positionId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "CUSIP")
    private String cusip;

    @Column(name = "position")
    private int position;

    @Column(name = "version")
    private int version;

    public Positions() {
    }

    public Positions(String accountNumber, String cusip, int position, int version) {
        this.accountNumber = accountNumber;
        this.cusip = cusip;
        this.position = position;
        this.version = version;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}