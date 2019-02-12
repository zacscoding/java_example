package demo.blockchain;

import demo.event.BlockEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleBlockchain extends Thread implements Blockchain {

    private String networkName;
    private long bestBlockNumber;
    private Map<Long, String> blockStore;
    private List<BlockEventListener> listeners;

    public SimpleBlockchain(String networkName) {
        this.bestBlockNumber = 0L;
        this.networkName = networkName;
        this.blockStore = new HashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addListener(BlockEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                BlockEvent newBlock = BlockEvent.builder()
                    .networkName(networkName)
                    .blockNumber(bestBlockNumber++)
                    .hash(UUID.randomUUID().toString().replace("-", ""))
                    .build();

                blockStore.put(newBlock.getBlockNumber(), newBlock.getHash());

                for (BlockEventListener listener : listeners) {
                    listener.onBlock(newBlock);
                }

                TimeUnit.SECONDS.sleep(5L);
            }
        } catch (InterruptedException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}