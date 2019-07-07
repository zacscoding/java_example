package paths;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class WindowPathTest {


    @Test
    public void testPathsWithSlash() throws Exception {
        File configDir = new File("src/test/resources/config");
        String absolutePath = configDir.getAbsolutePath();
        System.out.println("AbsolutePath :: " + absolutePath);

        Path configDirPath = Paths.get(absolutePath);
        System.out.println("Path :: " + configDirPath.toString());

        assertTrue(configDirPath.toFile().exists());

        String configurerPathValue = absolutePath + "/configurer.json";
        Path configurerPath = Paths.get(configurerPathValue);
        System.out.println("Configurer path :: " + configurerPath);

        assertTrue(configurerPath.toFile().exists());
    }

}
