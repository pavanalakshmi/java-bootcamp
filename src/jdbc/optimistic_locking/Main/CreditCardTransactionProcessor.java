package jdbc.optimistic_locking.Main;

import com.zaxxer.hikari.HikariDataSource;
import jdbc.optimistic_locking.config.HikariConfig;
import jdbc.optimistic_locking.model.CreditCardTransaction;
import jdbc.optimistic_locking.service.TransactionConsumerService;
import jdbc.optimistic_locking.util.TransactionFileReader;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreditCardTransactionProcessor {

    // HikariCP DataSource
    private static HikariDataSource dataSource;
    private static BlockingDeque<CreditCardTransaction> creditCardTransactionQueue;
    private static String filePath = "/Users/akm/pavani/JavaBootcamp/credit_card_transactions.txt";

    public static void main(String[] args) {
        try{
            // Step 1: Configure HikariCP connection pool
            HikariConfig.configureHikariCP();
            // Step 2: Read file and load transactions into ArrayBlockingQueue
//        TransactionFileReader.readTransactionFileAndWriteToQueue("");
            startMultiThreadedProcessing();
            creditCardTransactionQueue = TransactionFileReader.readTransactionFileAndWriteToQueue(filePath);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void startMultiThreadedProcessing() {
        // Step 3: Start multiple consumer threads to process transactions
        try {
            int numberOfThreads = 5;
            // Creating a Fixed Thread Pool - Each task submitted to the pool will be executed by one of the threads in the pool.
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
            for (int i = 0; i < numberOfThreads; i++) {
                // submitting instances of TransactionConsumer class, which processes cc transactions from the creditCardTransactionQueue using the dataSource.
                executorService.submit(new TransactionConsumerService(creditCardTransactionQueue, dataSource));
            }
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
