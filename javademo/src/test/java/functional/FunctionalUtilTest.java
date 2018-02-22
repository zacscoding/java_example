package functional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;
import org.junit.Test;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class FunctionalUtilTest {

    @Test
    public void adderAndComposition() {
        // Test Adder
        Function<Integer, Function<Integer, Integer>> makeAdder = FunctionalUtil::adder;
        assertTrue(makeAdder.apply(1).apply(2) == 3);

        Function<Integer, Integer> add1 = x -> x + 1;
        Function<Integer, Integer> mult3 = x -> x * 3;

        assertTrue(add1.apply(2) == 3);
        assertTrue(mult3.apply(2) == 6);
        assertTrue(mult3.apply(add1.apply(2)) == 9);

        // BinaryOperator test
        BinaryOperator<Integer> sum = (a, b) -> a + b;
        assertTrue(sum.apply(2, 3) == 5);

        // Composition strategy 1)
        BinaryOperator<Function<Integer, Integer>> compose = (f, g) -> x -> g.apply(f.apply(x));
        Function<Integer, Integer> h = compose.apply(add1, mult3);
        assertTrue(h.apply(2) == 9);

        // Composition strategy 2)
        Function<Integer, Integer> h2 = mult3.compose(add1);
        assertTrue(h2.apply(2) == 9);

        // Composition test2)
        Function<Integer, Double> f = x -> {
            String str = String.valueOf(x);
            str += "." + String.valueOf(x);
            return Double.parseDouble(str);
        };
        Function<Double, String> g = x -> {
            return String.valueOf(x);
        };
        assertThat(g.compose(f).apply(11), is("11.11"));
    }

    @Test
    public void partialFunctional() {
        Function<Integer, Function<Integer, Integer>> sum = x -> y -> x + y;
        Function<Integer, Integer> plus10 = sum.apply(10);
        assertTrue(plus10.apply(5) == 15);

        // unary(단항) funcs
        UnaryOperator<Integer> add1 = x -> x + 1;
        assertTrue(add1.apply(10) == 11);
        Function<Integer, UnaryOperator<Integer>> sum2 = x -> y -> x + y;
        UnaryOperator<Integer> plus11 = sum2.apply(11);
        assertTrue(plus11.apply(5) == 16);
    }

    @Test
    public void interfaceTest() {
        IntFx add1 = x -> x + 1;
        assertTrue(add1.apply(10) == 11);

        IntFunction<IntFx> sum = x -> y -> x + y;
        IntFunction<IntFx> sum2 = new IntFunction<IntFx>() {
            @Override
            public IntFx apply(int x) {
                return new IntFx() {
                    @Override
                    public int apply(int y) {
                        return x + y;
                    }
                };
            }
        };

        IntFx sum10 = sum.apply(10);
        assertTrue(sum10.apply(5) == 15);


    }
}


