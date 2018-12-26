package guava.graph;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-12-26
 * @GitHub : https://github.com/zacscoding
 */
public class CycleTest {

    @Test
    public void cycle() throws Exception {
        long start = System.currentTimeMillis();
        MutableGraph<Integer> graph = GraphBuilder.directed().build();
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.putEdge(1, 2);
        graph.putEdge(2, 3);
        System.out.println(Graphs.hasCycle(graph));
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(elapsed);
    }
}
