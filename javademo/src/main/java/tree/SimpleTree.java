package tree;

import java.util.function.Consumer;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public interface SimpleTree<T> {

    /**
     * add child at current node
     */
    public void add(T t);

    /**
     * complete current node
     */
    public void complete();

    /**
     * get root node
     */
    public SimpleNode<T> getRoot();

    /**
     * get current node
     */
    public SimpleNode<T> getCurrentNode();

    /**
     * get current data
     */
    public T getCurrentData();

    public void traversal(Traversal type, Consumer<SimpleNode<T>> consumer);
}
