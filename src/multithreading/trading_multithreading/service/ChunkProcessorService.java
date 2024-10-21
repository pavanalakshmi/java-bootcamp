package multithreading.trading_multithreading.service;

import com.zaxxer.hikari.HikariDataSource;
import multithreading.trading_multithreading.config.HikariCPConfig;
import multithreading.trading_multithreading.dao.PayloadDAO;
import multithreading.trading_multithreading.factory.BeanFactory;
import multithreading.trading_multithreading.util.ApplicationConfigProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ChunkProcessorService implements ChunkProcessor {
    private final ExecutorService chunkProcessorExecutorService;
    HikariDataSource dataSource;
    TradeDistributorMapService tradeDistributorMap;
    TradeDistributionQueueService tradeDistributionQueue;
    private static ApplicationConfigProperties applicationConfigProperties;
    private final LinkedBlockingQueue<String> chunkQueue;
    private static PayloadDAO payloadDAO;

    public ChunkProcessorService(LinkedBlockingQueue<String> chunkQueue, TradeDistributionQueueService tradeDistributionQueueService) {
        this.chunkQueue = chunkQueue;
        applicationConfigProperties = ApplicationConfigProperties.getInstance();
        chunkProcessorExecutorService = Executors.newFixedThreadPool(applicationConfigProperties.getChunkProcessorThreadPoolSize()); // Create a thread pool of size 10
        dataSource = HikariCPConfig.getDataSource();
        tradeDistributorMap = new TradeDistributorMapService();
        this.tradeDistributionQueue = tradeDistributionQueueService;
        payloadDAO = BeanFactory.getPayloadDAO();
    }

    public void chunksProcessor() {
        try {
            int emptyPollCount = 0;
            int maxEmptyPolls = 5;
            String criteria = applicationConfigProperties.getDistributionLogicCriteria();

            while (true){
                String file = chunkQueue.poll(500, TimeUnit.MILLISECONDS);
                if(file==null){
                    emptyPollCount++;
                    if (emptyPollCount >= maxEmptyPolls) {
                        System.out.println("No more files to process, exiting...");
                        break;
                    }
                    continue;
                }
                emptyPollCount = 0;
                chunkProcessorExecutorService.submit(() -> {
                    try {
                        processChunk(file);
                        if(applicationConfigProperties.getUseMap()){
                            if(criteria.equals("tradeId")){ //10k
                                tradeDistributorMap.distributeMapWithTradeId(file); // size - 9992
                            } else if (criteria.equals("accountNumber")) {  // 9992
                                tradeDistributorMap.distributeMapWithAccountNumber(file);
                            }
                            tradeDistributionQueue.distributeQueue(file, tradeDistributorMap.getTradeMap());
                        } else{
                            tradeDistributionQueue.distributeQueueWithoutMap(file);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Error in chunk processor: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            chunkProcessorExecutorService.shutdown();
            try {
                if (!chunkProcessorExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                        chunkProcessorExecutorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                chunkProcessorExecutorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
                if (dataSource != null && !dataSource.isClosed()) {
                    dataSource.close();
                }
        }
    }

    public void processChunk(String file) throws IOException {
        String line;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            while ((line = fileReader.readLine()) != null) {
                payloadDAO.insertIntoPayload(line);
            }
        }
    }
}