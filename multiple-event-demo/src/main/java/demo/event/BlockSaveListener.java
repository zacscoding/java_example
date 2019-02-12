package demo.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import demo.db.BlockchainStore;
import demo.event.BlockEvent;
import demo.event.BlockPublisher;
import demo.leader.LeaderSelector;
import java.util.concurrent.TimeUnit;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class BlockSaveListener {

    private BlockchainStore blockchainStore;
    private LeaderSelector leaderSelector;

    public BlockSaveListener(BlockchainStore blockchainStore, LeaderSelector leaderSelector,
        BlockPublisher blockPublisher) {

        this.blockchainStore = blockchainStore;
        this.leaderSelector = leaderSelector;
        blockPublisher.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onBlock(BlockEvent blockEvent) {
        String jobId = blockEvent.getNetworkName() + blockEvent.getBlockNumber();
        int order = leaderSelector.getJobOrder(jobId);

        try {
            if (!leaderSelector.isTakenLeadership(order)) {
                int sleepSeconds = Math.min(order, 5);
                System.out.printf("[%s] Received block : %s but could not took leadership. so wait %d sec.\n",
                    getThreadName(), blockEvent, sleepSeconds);
                TimeUnit.SECONDS.sleep(sleepSeconds);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("InterruptedException exception occur");
            return;
        }

        if (blockchainStore.existBlock(blockEvent.getBlockNumber())) {
            System.out.printf("## [%s] Skip : %s because already stored\n", getThreadName(), blockEvent);
            return;
        }

        if (blockchainStore.save(blockEvent)) {
            System.out.printf("[%s] Success to save %s\n", getThreadName(), blockEvent);
        }
    }

    private String getThreadName() {
        Thread currentThread = Thread.currentThread();
        return currentThread.getName() + "-" + currentThread.getId();
    }
}
