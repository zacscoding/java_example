package json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class JsonNodeCopyTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void cloneTest() throws Exception {
        String json = IOUtils.toString(new File("src/test/resources/json/jackson/fabric-sysconfig.json").toURI(), StandardCharsets.UTF_8);
        JsonNode originNode = objectMapper.readTree(json);
        long start = System.currentTimeMillis();
        // deepCopy
        JsonNode copyNode = originNode.deepCopy();
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Deep copy elapsed : " + elapsed + " [ms]");

        // change origin value
        ((ObjectNode) originNode).put("sequence", "1");

        // asserts
        Assert.assertFalse(originNode.equals(copyNode));
        Assert.assertFalse(originNode.get("sequence").asText().equals(copyNode.get("sequence").asText()));

        Assert.assertTrue(originNode.get("channel_group").toString().equals(copyNode.get("channel_group").toString()));
    }
}
