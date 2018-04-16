package functional;

import java.util.function.BooleanSupplier;

/**
 * Task something with try count
 *
 * @author zacconding
 * @Date 2018-04-16
 * @GitHub : https://github.com/zacscoding
 */
public class TaskWithTryCount {

    private static int temp = 0;

    private static boolean taskWithTryCount(int tryCnt, long sleep, BooleanSupplier task) {
        if (tryCnt <= 0) {
            return false;
        }

        boolean success = false;
        boolean useSleep = sleep > 0L;

        while (tryCnt-- > 0 && !(success = task.getAsBoolean())) {
            if(useSleep) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                }
            }
        }

        return success;
    }

    private static boolean doSomething() {
        temp++;
        return temp >= 2;
    }

    public static void main(String[] args) {
        temp = 0;
        boolean result = taskWithTryCount(3,0L, () -> doSomething());
        System.out.println(result);


        temp = 0;
        result = taskWithTryCount(2, 0L, () -> doSomething());
        System.out.println(result);

        temp = 0;
        result = taskWithTryCount(1, 0L, () -> doSomething());
        System.out.println(result);
    }
}