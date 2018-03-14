package ch02.behavior;

import ch02.domain.Apple;

/**
 * @author zacconding
 * @Date 2018-03-14
 * @GitHub : https://github.com/zacscoding
 */
public class AppleHeavyWeightPredicate implements ApplePredicate {

    /**
     * select only heavy apples
     */
    @Override
    public boolean test(Apple apple) {
        return apple.getWeight() > 150;
    }
}
