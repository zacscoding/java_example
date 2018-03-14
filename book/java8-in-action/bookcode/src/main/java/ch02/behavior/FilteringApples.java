package ch02.behavior;

import ch02.domain.Apple;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zacconding
 * @Date 2018-03-14
 * @GitHub : https://github.com/zacscoding
 */
public class FilteringApples {

    /**
     * Attempts 1 : filtering green apples
     *
     * => green 이외의 filtering을 할 경우 계속 메소드 생성 해야 함.
     */
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            // condition : green apple 인지
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }

        return result;
    }

    /**
     * Attempts 2 : parameterizing the color
     */
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            // condition
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            // condition
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }

        return result;
    }

    /**
     * Attempts 3 : filtering with every attribute you can think of
     */
    public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if ((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
                result.add(apple);
            }
        }

        return result;
    }

    /**
     * Attempts 4 : filtering by abstract criteria
     *
     * => filterApples method의 behavior를 parameterized
     */
    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate predicate) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        /** Attempts 5 : using an anonymous class*/
        List<Apple> redApples = filterApples(apples, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });

        /**
         * Attempts 5 : using a lambda expression
         * => 익명 클래스를 사용하면 boilerplate 코드 많음
         */
        List<Apple> results = filterApples(apples, (Apple apple) -> "red".equals(apple.getColor()));
    }

}
