package antpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import util.SimpleLoggers;

/**
 * @author zacconding
 * @Date 2018-02-01
 * @GitHub : https://github.com/zacscoding
 */
public class PathTest {

    List<Pair<String, List<String>>> pairs;
    org.apache.shiro.util.AntPathMatcher shiroMatcher = new org.apache.shiro.util.AntPathMatcher();
    org.springframework.util.AntPathMatcher springMatcher = new org.springframework.util.AntPathMatcher();

    @Before
    public void setUp() {
        Pair<String, List<String>> p1 = new Pair<>(
            "/context/ant/?*.info",
            Arrays.asList(
                "/context/ant/123.info",
                "/context/ant/.info"
            )
        );

        Pair<String, List<String>> p2 = new Pair<>(
            "/context/ant/f?00.fq",
            Arrays.asList(
                "/context/ant/fa00.fq",
                "/context/ant/f00.fq"
            )
        );
        Pair<String, List<String>> p3 = new Pair<>(
            "/*",
            Arrays.asList(
                "/context/ant/fa00.fq",
                "/context/ant/f00.fq"
            )
        );
        Pair<String, List<String>> p4 = new Pair<>(
            "/**",
            Arrays.asList(
                "/context/ant/fa00.fq",
                "/context/ant/f00.fq"
            )
        );
        Pair<String, List<String>> p5 = new Pair<>(
            "/",
            Arrays.asList(
                "/context/ant/fa00.fq",
                "/context/ant/f00.fq"
            )
        );
        Pair<String, List<String>> p6 = new Pair<>(
            "/context/path",
            Arrays.asList(
                "/context/path?path=test&time=null",
                "/context/path"
            )
        );

        pairs = new ArrayList<>();
        // pairs.add(p1);
        // pairs.add(p2);
        // pairs.add(p3);
        // pairs.add(p4);
        // pairs.add(p5);
        pairs.add(p6);
    }

    @Test
    public void test() {
        int repeatCount = 1;
        for (int i = 0; i < repeatCount; i++) {
            System.out.println("=====================================================================================================================");
            for (Pair<String, List<String>> pair : pairs) {
                String pattern = pair.getFirst();
                List<String> uris = pair.getSecond();
                for (String uri : uris) {
                    long startTime = System.nanoTime();
                    boolean shiroResult = shiroMatcher.match(pattern, uri);
                    long shiroTIme = System.nanoTime() - startTime;
                    startTime = System.nanoTime();
                    boolean springResult = springMatcher.match(pattern, uri);
                    long springTime = System.nanoTime() - startTime;
                    SimpleLoggers.println("## check pattern pattern : {} , uri : {}, shiroResult : {}, springResult : {}, shiroTIme : {}, springTIme: {}, shiro-spring : {}", pattern, uri, shiroResult,
                        springResult, shiroTIme, springTime, (shiroTIme - springTime));
                }
            }
            System.out.println("=====================================================================================================================");
        }
    }
}

class Pair<K, V> {

    private K first;
    private V second;

    public Pair() {
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public V getSecond() {
        return this.second;
    }

    public void setSecond(V second) {
        this.second = second;
    }
}