package functional.ref;

import java.util.function.Consumer;
import org.junit.Test;

/**
 * source-code :: https://github.com/Kevin-Lee/modern-java-untold/blob/master/src/main/java/cc/kevinlee/modernjava/e03_consumer/ConsumerExamples.java
 */
public class ConsumerExample {

    @Test
    public void consumer() {
        final Consumer<String> print = s -> System.out.println(s);
        print.accept("Test");

        final Consumer<String> reverse = s -> System.out.println(new StringBuilder(s).reverse().toString());
        reverse.accept("ReversE");
    }
}
