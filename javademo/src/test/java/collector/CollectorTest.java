package collector;

import org.junit.Test;
import util.ThreadUtil;

/**
 * @author zacconding
 * @Date 2018-02-11
 * @GitHub : https://github.com/zacscoding
 */
public class CollectorTest {

    @Test
    public void test() {
        int actionPerSec = 10;
        for (int i = 0; i < 100; i++) {
            TestCollector.getInstance().setMessage("Message" + i);
            // int wait = randomInt(1, 20);
            //System.out.println("wait " + wait);
            ThreadUtil.sleep(randomInt(0, 3000));
        }

        ThreadUtil.sleep(60000L);
    }

    private int randomInt(int start, int range) {
        return (int) (Math.random() * range) + start;
    }

}
