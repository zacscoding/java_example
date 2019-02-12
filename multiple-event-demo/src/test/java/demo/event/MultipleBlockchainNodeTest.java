package demo.event;

import demo.blockchain.SimpleBlockchain;
import demo.db.BlockchainStore;
import demo.leader.LeaderSelector;
import demo.leader.LocalCacheLeaderSelector;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class MultipleBlockchainNodeTest {

    BlockchainStore blockchainStore;
    BlockPublisher blockPublisher;
    LeaderSelector leaderSelector;
    BlockSaveListener blockSaveListener;

    @Before
    public void setUp() {
        blockchainStore = new BlockchainStore();
        blockPublisher = new BlockPublisher();
        leaderSelector = new LocalCacheLeaderSelector();
        blockSaveListener = new BlockSaveListener(blockchainStore, leaderSelector, blockPublisher);
    }

    @Test
    public void listenMultipleBlockchainNode() throws Exception {
        String networkName = "private";
        int nodeCount = 5;
        SimpleBlockchain[] nodes = new SimpleBlockchain[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new SimpleBlockchain(networkName);
            nodes[i].addListener(event -> blockPublisher.publish(event));
            nodes[i].start();
        }

        TimeUnit.MINUTES.sleep(1L);

        for (int i = 0; i < nodeCount; i++) {
            nodes[i].interrupt();
        }

        System.out.printf("## Best block number : %d / Total save tried count : %d",
            blockchainStore.getBestBlockNumber(), blockchainStore.saveTriedCount()
        );
    }

}
