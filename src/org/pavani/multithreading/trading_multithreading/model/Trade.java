package org.pavani.multithreading.trading_multithreading.model;

public record Trade(String accountNumber, String cusip, String direction, int quantity, String tradeId) {

}
