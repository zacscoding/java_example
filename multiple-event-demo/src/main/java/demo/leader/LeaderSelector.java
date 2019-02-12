package demo.leader;

/**
 * @GitHub : https://github.com/zacscoding
 */
public interface LeaderSelector {

    int leaderNumber = 0;

    /**
     * Get order about this job id that started with 0 and increase by 1 during cache time
     */
    int getJobOrder(String jobId);

    default boolean isTakenLeadership(int order) {
        return order == leaderNumber;
    }
}