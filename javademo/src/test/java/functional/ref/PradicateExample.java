package functional.ref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.junit.Test;
import util.SimpleLoggers;

/**
 * source-code :  https://github.com/Kevin-Lee/modern-java-untold/blob/master/src/main/java/cc/kevinlee/modernjava/e04_predicate/PredicateExamples.java
 */
public class PradicateExample {

    @Test
    public void predicateExamples() {
        final Predicate<Integer> isPositive = i -> i > 0;
        SimpleLoggers.println("Test 1 : {} , 0 : {}, -1 : {}", isPositive.test(1), isPositive.test(0), isPositive.test(-1));
        List<Integer> numbers = Arrays.asList(-1, -2, 0, 10, 20, 3);
        List<Integer> positives = filter(numbers, isPositive);
        SimpleLoggers.printJSONPretty(positives);
    }

    private <T> List<T> filter(final List<T> list, final Predicate<T> filter) {
        final List<T> newList = new ArrayList<>();
        for (final T input : list) {
            if (filter.test(input)) {
                newList.add(input);
            }
        }

        return newList;
    }
}
