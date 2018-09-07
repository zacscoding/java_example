package exec.common;

import java.util.concurrent.TimeUnit;

/**
 * @author zacconding
 * @Date 2018-09-07
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("args must be not empty.");
            System.exit(-1);
        }

        try {
            int sleep = Integer.parseInt(args[0]);
            System.out.println("Start");
            for (int i = 0; i < sleep; i++) {
                System.out.println("Sleep ... " + (i + 1));
                TimeUnit.SECONDS.sleep(1);
            }
            System.out.println("Complete");
        } catch (NumberFormatException e) {
            System.err.println("args[0] must be Number");
            System.exit(-2);
        } catch (InterruptedException e) {
            System.exit(-3);
        }
    }
}
