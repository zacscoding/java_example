package tree;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class SimpleTreeImpl<T> implements SimpleTree<T> {

    private SimpleNode<T> root;
    private SimpleNode<T> current;

    @Override
    public void add(T t) {
        if (root == null) {
            root = new SimpleNode<>(t);
            current = root;
        } else {
            if (current != null) {
                SimpleNode<T> newNode = new SimpleNode<T>(t);
                current.addChild(newNode);
                current = newNode;
            } else {
                System.out.println("Current is null!!");
            }
        }
    }

    @Override
    public void complete() {
        if (current != null) {
            current = current.getParent();
        }
    }

    @Override
    public SimpleNode<T> getRoot() {
        return root;
    }

    @Override
    public SimpleNode<T> getCurrentNode() {
        return current == null ? null : current;
    }

    @Override
    public T getCurrentData() {
        return current == null ? null : current.getData();
    }

    @Override
    public void traversal(Traversal type, Consumer<SimpleNode<T>> consumer) {
        prefixTraversal(type, 0, this.root, consumer);
    }

    private void prefixTraversal(Traversal type, int depth, SimpleNode<T> node, Consumer<SimpleNode<T>> consumer) {
        if (node == null) {
            return;
        }

        node.setDeps(depth);
        if (type == Traversal.PREFIX) {
            consumer.accept(node);
        }

        List<SimpleNode<T>> childs = node.getChildren();

        if (childs != null && childs.size() > 0) {
            for (SimpleNode<T> child : childs) {
                prefixTraversal(type, depth + 1, child, consumer);
            }
        }

        if (type == Traversal.SUFFIX) {
            consumer.accept(node);
        }
    }
}
