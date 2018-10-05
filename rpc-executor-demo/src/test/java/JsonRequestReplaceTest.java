import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-10-05
 * @GitHub : https://github.com/zacscoding
 */
public class JsonRequestReplaceTest {

    List<String> rawRequests;
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        rawRequests = Arrays.asList(
            "{\"method\":\"eth_getBalance\",\"params\":[\"0x407d73d8a49eeb85d32cf465507dd71d507100c1\"],\"id\":1,\"jsonrpc\":\"2.0\"}",
            "{\"method\":\"eth_getBlockByHash\",\"params\":[\"0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d15273311\",true],\"id\": \"12@@\",\"jsonrpc\":\"2.0\"}"
        );
    }

    @Test
    public void idReplaceTest() {
        for (String rawRequest : rawRequests) {
            long start = System.currentTimeMillis();
            String replaced = replace(rawRequest);
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(">> Origin : \n" + rawRequest);
            System.out.println(">> Replaced : \n" + replaced);
            System.out.println(elapsed + " [MS]");
        }
    }

    @Test
    public void temp() throws Exception {
        for (String rawRequest : rawRequests) {
            System.out.println(">> Origin : \n" + rawRequest);

            JsonNode rootNode = objectMapper.readTree(rawRequest);
            JsonNode idNode = rootNode.get("id");

            ((ObjectNode) rootNode).put("id", Long.valueOf(10L));
            System.out.println(">> Changed request : \n" + rootNode.toString());


            ((ObjectNode) rootNode).put("id", idNode);
            System.out.println(">> Recovery request : \n" + rootNode.toString());
        }

        /*JsonNode rootNode2 = requestForId.get(replacedId).getOriginRootNode();
        ((ObjectNode) rootNode).put("id", requestForId.get(replacedId).getOriginIdNode());
        return rootNode2.toString();*/
    }

    private String replace(String rawRequest) {
        try {
            JsonNode rootNode = objectMapper.readTree(rawRequest);
            // ((ObjectNode) rootNode).put("id", Long.valueOf(10));
            ((ObjectNode) rootNode).put("id", rootNode.get("id"));
            return rootNode.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
