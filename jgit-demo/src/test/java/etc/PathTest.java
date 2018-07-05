package etc;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-07-04
 * @GitHub : https://github.com/zacscoding
 */
public class PathTest {

    @Test
    public void pathTest() throws Exception {
        Path p1 = Paths.get("E:\\jgit-test");
        Path p2 = Paths.get("E:\\jgit-test", "test1", "test.txt");

        // URI uri = p2.toUri();
        Path r = p1.relativize(p2);
        System.out.println(r.toString());
        System.out.println(r.toAbsolutePath());
        System.out.println(r.normalize().toString());

        /*System.out.println(uri.toString());
        System.out.println(uri.toASCIIString());
        System.out.println(uri.toURL().toExternalForm());
        System.out.println(uri.toURL().toString());*/
    }
}
