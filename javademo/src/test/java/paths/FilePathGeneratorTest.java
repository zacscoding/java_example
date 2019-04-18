package paths;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class FilePathGeneratorTest {

    @Test
    public void temp() {
        System.out.println(new String[10].length);
    }

    @Test
    public void testToFilePath() {
        String template = "/path1/{path2}/path3/{path4}";
        Map<String, Object> pathVariables = new HashMap<>();
        pathVariables.put("path4", "replacedPath4");

        String generatedPath = FilePathGenerator.builder()
            .path(template)
            .pathVariable("path2", "replacedPath2")
            .pathVariables(pathVariables)
            .build()
            .toFilePath();

        assertThat(generatedPath, is("/path1/replacedPath2/path3/replacedPath4"));
    }

    @Test(expected = RuntimeException.class)
    public void testToFilePathThrowExceptionWhenInvalidPathVariables() {
        FilePathGenerator.builder()
            .path("/path1/{path2}/path3/{path4}")
            .build()
            .toFilePath();
        fail();
    }

    @Test
    public void testToFilePathThrowExceptionWhenInvalidPath() {
        FilePathGenerator.builder()
            .path("/path1/{/dd")
            .build()
            .toFilePath();
    }
}
