package jdbc.optimistic_locking.service;

import com.zaxxer.hikari.HikariDataSource;
import jdbc.optimistic_locking.exception.OptimisticLockingException;
import jdbc.optimistic_locking.exception.SQLIntegrityConstraintViolationException;
import jdbc.optimistic_locking.model.CreditCardTransaction;
import jdbc.optimistic_locking.dao.TransactionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;

// Consumer thread that processes transactions
public class TransactionConsumerService implements Runnable {
    private BlockingDeque<CreditCardTransaction> creditCardTransactionQueue;
    private HikariDataSource dataSource;

    public TransactionConsumerService(BlockingDeque<CreditCardTransaction> creditCardTransactionQueue, HikariDataSource dataSource) {
        this.creditCardTransactionQueue = creditCardTransactionQueue;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        while (true) {
            if(creditCardTransactionQueue.isEmpty()){
                creditCardTransactionQueue = null;
                break;
            } else {
                try {
                    CreditCardTransaction creditCardTransaction = creditCardTransactionQueue.take();  // Get transaction from queue
                    processTransaction(creditCardTransaction);  // Process transaction
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Process each transaction with optimistic locking and retry logic
    private void processTransaction(CreditCardTransaction creditCardTransaction) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                // Step 1: Check if account exists
                int version = TransactionDAO.getAccountVersion(connection, creditCardTransaction.getCreditCardNumber());
                if (version == -1) {
                    // Step 2: If no account exists, insert it
                    TransactionDAO.insertAccount(connection, creditCardTransaction);
                } else {
                    // Step 3: Now update with optimistic locking
                    TransactionDAO.updateAccountBalance(connection, creditCardTransaction, version);
                }
                connection.commit();  // Commit transaction
                System.out.println("Transaction processed for card: " + creditCardTransaction.getCreditCardNumber());

            } catch (SQLIntegrityConstraintViolationException | OptimisticLockingException e) {
                System.err.println(e.getMessage());
                // Put the transaction back in the queue for retry
//                creditCardTransactionQueue.put(creditCardTransaction);
                creditCardTransactionQueue.putFirst(creditCardTransaction);
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


