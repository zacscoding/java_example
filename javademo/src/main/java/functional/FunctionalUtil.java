package functional;

import java.util.function.Function;

/**
 * ref : https://dzone.com/articles/functional-programming-java-8
 *
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class FunctionalUtil {

    public static Function<Integer, Integer> adder(Integer x) {
        return y -> x + y;
    }
}
