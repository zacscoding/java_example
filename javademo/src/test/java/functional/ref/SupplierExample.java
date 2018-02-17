package functional.ref;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.Test;
import util.SimpleLogger;

/**
 * source-code : https://github.com/Kevin-Lee/modern-java-untold/blob/master/src/main/java/cc/kevinlee/modernjava/e05_supplier/SupplierExamples.java
 */
public class SupplierExample {

    @Test
    public void basic() {
        final Supplier<String> helloSupplier = () -> "Hello";
        System.out.println(helloSupplier.get() + " Java");
    }

    @Test
    public void callingExpensiveMethodWithoutSupplier() {
        System.out.println("SupplierExamples.callingExpensiveMethodWithoutSupplier()");
        final long start = System.currentTimeMillis();
        printIfValidIndex(0, getVeryExpensiveValue());
        printIfValidIndex(-1, getVeryExpensiveValue());
        printIfValidIndex(-2, getVeryExpensiveValue());
        System.out.println("It took " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
    }

    @Test
    public void callingExpensiveMethodWithSupplier() {
        System.out.println("SupplierExamples.callingExpensiveMethodWithSupplier()");
        final long start = System.currentTimeMillis();
        printIfValidIndex(0, () -> getVeryExpensiveValue());
        printIfValidIndex(-1, () -> getVeryExpensiveValue());
        printIfValidIndex(-2, () -> getVeryExpensiveValue());
        System.out.println("It took " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
    }


    /**
     * 계산 하는데 오래 걸리는 메소드를 시뮬레이션 해봤습니다. 항상 3초가 걸려요.
     *
     * @return 항상 "Kevin"만 리턴
     */
    private String getVeryExpensiveValue() {
        SimpleLogger.println("getVeryExpensiveValue() is called Thread id : {}, name : {}", Thread.currentThread().getId(), Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Kevin";
    }

    private void printIfValidIndex(final int number, final String value) {
        if (number >= 0) {
            System.out.println("The value is " + value + ".");
        } else {
            System.out.println("Invalid");
        }
    }

    private void printIfValidIndex(final int number, final Supplier<String> valueSupplier) {
        if (number >= 0) {
            System.out.println("The value is " + valueSupplier.get() + ".");
        } else {
            System.out.println("Invalid");
        }
    }
}
