package nio.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
public class PathDisplayTest {

    @Test
    public void displayPath() {
        Path path = Paths.get("src/test/resources/nio-file-test");

        /*
        Path path = Paths.get("src/test/resources/nio-file-test");
        path.toString() : src\test\resources\nio-file-test
        path.getFileName().toString() : nio-file-test
        path.toString() : src\test\resources\nio-file-test
        path.normalize().toString() : src\test\resources\nio-file-test
        path.getNameCount() : 4
        path.isAbsolute() : false
        path.toUri().toString() : file:///C:/git/java_example/javademo/src/test/resources/nio-file-test/
        path.toAbsolutePath().toString() : C:\git\java_example\javademo\src\test\resources\nio-file-test
         */
        SimpleLogger.build()
                    .appendln("Path path = Paths.get(\"src/test/resources/nio-file-test\");")
                    .appendln("path.toString() : " +  path.toString())
                    .appendln("path.getFileName().toString() : " + path.getFileName().toString())
                    .appendln("path.toString() : " +  path.toString())
                    .appendln("path.normalize().toString() : " +  path.normalize().toString())
                    .appendln("path.getNameCount() : " +  path.getNameCount())
                    .appendln("path.isAbsolute() : " +  path.isAbsolute())
                    .appendln("path.toUri().toString() : " +  path.toUri().toString())
                    .appendln("path.toAbsolutePath().toString() : " +  path.toAbsolutePath().toString())
                    .flush();
    }
}
