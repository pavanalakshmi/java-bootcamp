package org.pavani.multithreading.trading_multithreading.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "journal_entry")
@Data
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID (optional)
    @Column(name = "id") // Maps to the 'id' column in the table
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "CUSIP")
    private String cusip;

    @Column(name = "direction")
    private String direction;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "posted_status")
    private String postedStatus;

    @Column(name = "timestamp1")
    private Timestamp timestamp;

    public JournalEntry() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPostedStatus() {
        return postedStatus;
    }

    public void setPostedStatus(String postedStatus) {
        this.postedStatus = postedStatus;
    }
}