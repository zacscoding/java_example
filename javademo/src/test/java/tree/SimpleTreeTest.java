package tree;

import java.util.function.Consumer;
import org.junit.Test;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class SimpleTreeTest {

    @Test
    public void traversal() {
        SimpleTree<String> tree = new SimpleTreeImpl<>();
        tree.add("A");
        tree.add("B");
        tree.add("C");
        // complete C
        tree.complete();
        tree.add("D");
        tree.add("E");
        // complete E
        tree.complete();
        // complete D
        tree.complete();
        // complete B
        tree.complete();
        tree.add("F");
        // complete F
        tree.complete();
        // complete A
        tree.complete();
        Consumer<SimpleNode<String>> consumer = elt -> System.out.println(elt.getDeps() + " :: " + elt.getData());
        System.out.println("dep|val");
        tree.traversal(Traversal.PREFIX, consumer);
    }

    @Test
    public void traversalTest() {
        SimpleTree<String> tree = new SimpleTreeImpl<>();
        tree.add("A");
        tree.add("B");
        tree.complete();
        tree.add("C");
        tree.complete();
        tree.complete();

        Consumer<SimpleNode<String>> consumer = elt -> System.out.println(elt.getDeps() + " :: " + elt.getData());
        tree.traversal(Traversal.PREFIX, consumer);
        System.out.println("================================================");
        tree.traversal(Traversal.SUFFIX, consumer);
    }

}
