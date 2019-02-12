package demo.leader;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class LocalCacheLeaderSelectorTest {

    LeaderSelector leaderSelector = new LocalCacheLeaderSelector();

    @Test
    public void getOrder() {
        int taskCount = 5;
        for (int i = 0; i < taskCount; i++) {
            String taskId = "task" + i;

            for (int j = 0; j < 5; j++) {
                boolean isLeader = j == 0;
                int order = leaderSelector.getJobOrder(taskId);
                assertTrue(leaderSelector.isTakenLeadership(order) == isLeader);
            }
        }
    }

    @Test
    public void getOrderConcurrency() throws InterruptedException {
        // given when
        int jobCount = 5;
        int concurrencyCount = 100;

        TestTask[][] tasks = new TestTask[jobCount][concurrencyCount];
        for (int i = 0; i < jobCount; i++) {
            String jobId = "task" + i;
            for (int j = 0; j < concurrencyCount; j++) {
                tasks[i][j] = new TestTask(leaderSelector, jobId);
                tasks[i][j].start();
            }
        }

        for (int i = 0; i < jobCount; i++) {
            for (int j = 0; j < concurrencyCount; j++) {
                tasks[i][j].join();
            }
        }

        // then
        for (int i = 0; i < jobCount; i++) {
            int leaderCount = 0;
            for (int j = 0; j < concurrencyCount; j++) {
                if (tasks[i][j].isLeader()) {
                    leaderCount++;
                }
            }
            assertTrue(leaderCount == 1);
        }
    }

    private static class TestTask extends Thread {

        private LeaderSelector selector;
        private String jobId;
        private boolean isLeader = false;

        public TestTask(LeaderSelector selector, String jobId) {
            this.selector = selector;
            this.jobId = jobId;
        }

        @Override
        public void run() {
            int order = selector.getJobOrder(jobId);
            isLeader = selector.isTakenLeadership(order);
        }

        public boolean isLeader() {
            return isLeader;
        }
    }
}
