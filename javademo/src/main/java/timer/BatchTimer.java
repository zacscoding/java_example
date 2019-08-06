package timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import util.SimpleLogger;

/**
 *
 */
public class BatchTimer {

    private long blockNumber;
    private int activeNodeCount;
    private int remains;

    Timer timer;
    TimerTask timerTask;
    long timeoutMillis;

    private ConcurrentHashMap<String, List<String>> nodes = new ConcurrentHashMap<>();

    public BatchTimer(long blockNumber, int activeNodeCount, long timeout, TimeUnit timeUnit) {
        this.blockNumber = blockNumber;
        this.activeNodeCount = activeNodeCount;
        this.remains = activeNodeCount;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(">> Cutting block becuz of timeout");
                cuttingBlocks();
            }
        };
        timeoutMillis = timeUnit.toMillis(timeout);
    }

    public void newBlock(String blockHash, String node) {
        // first come
        if (nodes.isEmpty()) {
            timer.schedule(timerTask, timeoutMillis, timeoutMillis);
        }

        List<String> nodesByHash = nodes.get(blockHash);

        if (nodesByHash == null) {
            nodesByHash = new ArrayList<>(activeNodeCount);
            nodes.put(blockHash, nodesByHash);
        }

        nodesByHash.add(node);

        remains--;

        if (remains == 0) {
            cuttingBlocks();
        }
    }

    private void cuttingBlocks() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-dd");
        long time = System.currentTimeMillis();
        SimpleLogger.info("## cutting blocks..");

        if (timer != null) {
            timer.cancel();
        }

        if (nodes.isEmpty()) {
            SimpleLogger.print("## Received empty block..");
            return;
        }

        if (nodes.size() == 1) {
            Entry<String, List<String>> entry = nodes.entrySet().iterator().next();
            SimpleLogger.println("## Received same hash {} from {} nodes."
                , entry.getKey(), entry.getValue().size());

            return;
        }

        SimpleLogger.println("## Received diff hash {}", nodes.size());
        for (Entry<String, List<String>> entry : nodes.entrySet()) {

            StringBuilder nodesString = new StringBuilder();
            for (String nodeName : entry.getValue()) {
                nodesString.append(nodeName)
                    .append(" ");
            }

            SimpleLogger.println("> Hash : {} ==> {}", entry.getKey(), nodesString.toString());

        }
    }
}
