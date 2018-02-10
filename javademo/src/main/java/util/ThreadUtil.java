package util;

/**
 * @author zacconding
 * @Date 2018-02-10
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadUtil {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
