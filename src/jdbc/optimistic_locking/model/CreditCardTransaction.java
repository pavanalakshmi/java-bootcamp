package jdbc.optimistic_locking.model;

public class CreditCardTransaction {
    private String creditCardNumber;
    private String cardType;
    private String transactionType;
    private double amount;
    private double balance;

    public CreditCardTransaction(String creditCardNumber, String cardType, String transactionType, double amount, double balance) {
        this.creditCardNumber = creditCardNumber;
        this.cardType = cardType;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balance = balance;
    }

    // Getters
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "creditCardNumber='" + creditCardNumber + '\'' +
                ", cardType='" + cardType + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", balance=" + balance +
                '}';
    }

}
