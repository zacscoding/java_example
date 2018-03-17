package ch03;

import ch02.domain.Apple;
import java.util.Comparator;

/**
 * @author zacconding
 * @Date 2018-03-18
 * @GitHub : https://github.com/zacscoding
 */
public class LambdaBasic {

    public void howToUse() {
        // before
        Comparator<Apple> byWeight = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        };

        // after
        // Lambda parameters, Arrow, Labmda Body로 이루어짐
        Comparator<Apple> byWeight2 = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
    }
}
