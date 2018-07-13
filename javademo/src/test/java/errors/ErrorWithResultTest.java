package errors;

import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-07
 * @GitHub : https://github.com/zacscoding
 */
public class ErrorWithResultTest {

    @Test
    public void test() {
        parseInt("1");
        parseInt("aa");
    }

    private void parseInt(String value) {
        System.out.println("// =========================================================================");

        System.out.println("Try to parse : " + value);
        ResultWithError<Integer> result = ResultWithErrorSample.parseInt(value);
        if (result.isError()) {
            System.out.println("Failed to parse : " + result.getError().getMessage());
        } else {
            System.out.println("Success to parse : " + result.getResult());
        }

        System.out.println("========================================================================= //\n");
    }
}