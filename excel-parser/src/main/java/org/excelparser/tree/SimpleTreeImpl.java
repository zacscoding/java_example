package org.excelparser.tree;

import java.util.Comparator;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class SimpleTreeImpl<T> implements ITree<T> {

    private Comparator<T> comparator;

    private SimpleTreeImpl(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public static <T> ITree<T> getInstance(Comparator<T> comparator) {
        return new SimpleTreeImpl<>(comparator);
    }




}
