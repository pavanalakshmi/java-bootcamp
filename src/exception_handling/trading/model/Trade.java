package exception_handling.trading.model;

import java.sql.Date;

public class Trade {

    private String tradeId;
    private String tradeIdentifier;
    private String tickerSymbol;
    private int quantity;
    private Double price;
    private Date trade_date;

    public Trade() {
    }

    public Trade(String tradeId, String tradeIdentifier, String tickerSymbol, int quantity, Double price, Date trade_date) {
        this.tradeId = tradeId;
        this.tradeIdentifier = tradeIdentifier;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.price = price;
        this.trade_date = trade_date;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeIdentifier() {
        return tradeIdentifier;
    }

    public void setTradeIdentifier(String tradeIdentifier) {
        this.tradeIdentifier = tradeIdentifier;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(Date trade_date) {
        this.trade_date = trade_date;
    }


    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                "tradeIdentifier='" + tradeIdentifier + '\'' +
                ", tickerSymbol='" + tickerSymbol + '\'' +
                ", quantity='" + quantity + '\'' +
                ", amount=" + price +
                ", trade_date=" + trade_date +
                '}';
    }

}
