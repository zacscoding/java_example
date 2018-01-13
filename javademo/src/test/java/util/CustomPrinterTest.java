package util;

import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-01-14
 * @GitHub : https://github.com/zacscoding
 */
public class CustomPrinterTest {

    @Test
    public void printTest() {
//        CustomPrinter.print("TEST > ");
//
//        CustomPrinter.print("TEST 1 : {} , 2 : {}", "TEST", 1);
        //System.out.println();
        CustomPrinter.println("TEST 1arg : {}, 2arg : {} , 3arg : {}" , 1, 2);

        CustomPrinter.println("TEST 1arg : {}, 2arg: {}" , 1, 2,3);

        CustomPrinter.println("TEST 1arg : {" , 1, 2,3);
    }
}
