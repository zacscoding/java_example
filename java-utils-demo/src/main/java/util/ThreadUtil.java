package util;

/**
 * @author zacconding
 * @Date 2018-08-30
 * @GitHub : https://github.com/zacscoding
 */
public class ThreadUtil {

    public static String getThreadInfo() {
        Thread t = Thread.currentThread();

        return t.getName() + "(" + t.getId() + ")";
    }

    private ThreadUtil() {
        throw new UnsupportedOperationException("Not supported to create instance");
    }
}