package org.pavani.multithreading.trading_multithreading.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "trade_payloads")
@Data
public class TradePayloads {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "trade_id")
    private String tradeId;

    @Column(name = "validity_status")
    private String validityStatus;

    @Column(name = "payload")
    private String payload;

    @Column(name = "lookup_status")
    private String lookupStatus;

    @Column(name = "je_status")
    private String jeStatus;

    public TradePayloads() {
    }

    public TradePayloads(int id, String tradeId, String validityStatus, String payload, String lookupStatus, String jeStatus) {
        this.id = id;
        this.tradeId = tradeId;
        this.validityStatus = validityStatus;
        this.payload = payload;
        this.lookupStatus = lookupStatus;
        this.jeStatus = jeStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getValidityStatus() {
        return validityStatus;
    }

    public void setValidityStatus(String validityStatus) {
        this.validityStatus = validityStatus;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getLookupStatus() {
        return lookupStatus;
    }

    public void setLookupStatus(String lookupStatus) {
        this.lookupStatus = lookupStatus;
    }

    public String getJeStatus() {
        return jeStatus;
    }

    public void setJeStatus(String jeStatus) {
        this.jeStatus = jeStatus;
    }
}
