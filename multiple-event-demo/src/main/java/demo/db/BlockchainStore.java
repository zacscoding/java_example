package demo.db;

import demo.event.BlockEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class BlockchainStore {

    private ConcurrentHashMap<Long, BlockEvent> blocks = new ConcurrentHashMap<>();
    private long bestBlockNumber = 0L;
    private AtomicInteger triedCount = new AtomicInteger(0);

    public boolean save(BlockEvent blockEvent) {
        if (bestBlockNumber < blockEvent.getBlockNumber()) {
            this.bestBlockNumber = blockEvent.getBlockNumber();
        }
        triedCount.incrementAndGet();
        return blocks.putIfAbsent(blockEvent.getBlockNumber(), blockEvent) == null;
    }

    public boolean existBlock(long blockNumber) {
        boolean result = blocks.containsKey(blockNumber);
        return result;
    }

    public long getBestBlockNumber() {
        return bestBlockNumber;
    }

    public int saveTriedCount() {
        return triedCount.get();
    }
}