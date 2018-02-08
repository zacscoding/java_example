package util;

import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-01-14
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleLoggerTest {

    @Test
    public void printTest() {
//        CustomPrinter.print("TEST > ");
//
//        CustomPrinter.print("TEST 1 : {} , 2 : {}", "TEST", 1);
        //System.out.println();
        SimpleLogger.println("TEST 1arg : {}, 2arg : {} , 3arg : {}", 1, 2);

        SimpleLogger.println("TEST 1arg : {}, 2arg: {}", 1, 2, 3);

        SimpleLogger.println("TEST 1arg : {", 1, 2, 3);
    }

    @Test
    public void infoAndError() {
        SimpleLogger.info("This is info");
        try {
            boolean temp = true;
            if (temp) {
                throw new RuntimeException("Temp Runtime Exception");
            }
        } catch (Throwable t) {
            SimpleLogger.error("test error", t);
        }
    }
}
