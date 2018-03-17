package ch03;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-03-18
 * @GitHub : https://github.com/zacscoding
 */
public class FunctionExample {

    public static void testPredicate() {
        System.out.println("Test predicate..");
        Predicate<String> nonEmptyStringPredicate = (String s) -> (s != null) && (!s.isEmpty());
        String[] samples = {"", "test", null};

        for (String sample : samples) {
            SimpleLogger.println("Check : {} , result : {}", sample, nonEmptyStringPredicate.test(sample));
        }
    }

    public static void testConsumer() {
        System.out.println("Test consumer..");
        Consumer<Integer> integerConsumer = (Integer i) -> System.out.println(i);

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        for (Integer integer : integers) {
            integerConsumer.accept(integer);
        }
    }

    public static void testFunction() {
        System.out.println("Test function..");
        List<String> strs = Arrays.asList("lambdas", "in", "action", null);
        Function<String, Integer> lengthFunction = (String s) -> s == null ? 0 : s.length();

        for (String str : strs) {
            SimpleLogger.println("Check : {} , result : {}", str, lengthFunction.apply(str));
        }
    }

    // Primitive specializations (boxing, unboxing 등 없이 기본 자료형으로 사용)
    public static void testIntPredicate() {
        System.out.println("Test IntPredicate");
        IntPredicate evenNumbers = i -> i % 2 == 0;
        IntStream.range(1, 5).forEach(i -> SimpleLogger.println("Check : {}, Result : {}", i, evenNumbers.test(i)));
    }


    public static void main(String[] args) {
        testPredicate();
        testConsumer();
        testFunction();
        testIntPredicate();
    }


}
