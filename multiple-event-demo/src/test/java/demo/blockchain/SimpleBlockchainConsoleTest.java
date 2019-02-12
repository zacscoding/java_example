package demo.blockchain;

import demo.blockchain.SimpleBlockchain;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleBlockchainConsoleTest {

    @Test
    public void start() throws InterruptedException {
        SimpleBlockchain blockchain = new SimpleBlockchain("private");
        blockchain.addListener(blockEvent -> System.out.println(blockEvent));

        Thread thread = new Thread(blockchain);
        thread.start();

        TimeUnit.SECONDS.sleep(15L);
    }
}
