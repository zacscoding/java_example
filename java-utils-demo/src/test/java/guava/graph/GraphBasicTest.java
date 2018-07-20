package guava.graph;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import java.util.Objects;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-20
 * @GitHub : https://github.com/zacscoding
 */
public class GraphBasicTest {

    @Test
    public void basic() {
        MutableGraph<GraphVertex> graph = GraphBuilder.undirected().build();
        GraphVertex v1 = new GraphVertex("Node1", 1);
        GraphVertex v2 = new GraphVertex("Node1", 2);
        GraphVertex v3 = new GraphVertex("Node3", 1);
        GraphVertex v4 = new GraphVertex("Node1", 1);

        assertTrue(graph.addNode(v1));
        assertTrue(graph.addNode(v2));
        assertTrue(graph.addNode(v3));
        assertFalse(graph.addNode(v4)); // same v1

        graph.putEdge(v1, v2);

        assertTrue(graph.degree(v1) == 1);
        assertTrue(graph.degree(v2) == 1);
        assertTrue(graph.degree(v3) == 0);

        assertTrue(graph.adjacentNodes(v1).contains(v2));
        assertFalse(graph.adjacentNodes(v1).contains(v3));
    }


    static class GraphVertex {

        String name;
        int age;

        public GraphVertex() {
        }

        public GraphVertex(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GraphVertex)) {
                return false;
            }
            GraphVertex that = (GraphVertex) o;
            return getAge() == that.getAge() && Objects.equals(getName(), that.getName());
        }

        @Override
        public String toString() {
            return "GraphVertex{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }
}
