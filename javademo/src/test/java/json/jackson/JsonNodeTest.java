package json.jackson;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zacconding
 * @Date 2018-06-30
 * @GitHub : https://github.com/zacscoding
 */
public class JsonNodeTest {

    List<String> jsonSamples;

    @Before
    public void setUp() throws Exception {
        jsonSamples = Arrays.asList(new ClassPathResource("json/jackson").getFile().listFiles())
                            .stream().filter(f -> f.getName().endsWith(".json"))
                            .map(f -> {
                                try {
                                    return Files.readAllLines(f.toPath()).stream().collect(Collectors.joining(""));
                                } catch (Exception e) {
                                    System.out.println("io error" + f.toString());
                                    return "{}";
                                } })
                            .collect(Collectors.toList());
    }

    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (String json : jsonSamples) {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode commandNode = rootNode.get("command");
            if (commandNode != null) {
                String command = commandNode.asText();
                if ("type1".equals(command)) {
                    JsonNode node = rootNode.get("parameters");

                    System.out.println("type1");
                    Type1 t1 = objectMapper.treeToValue(node, Type1.class);
                    System.out.println(t1.toString());
                } else if ("type2".equals(command)) {
                    JsonNode node = rootNode.get("parameters");

                    System.out.println("type2");
                    Type2 t2 = objectMapper.treeToValue(node, Type2.class);
                    System.out.println(t2.toString());
                } else {
                    System.out.println("Unknown command : " + command);
                }
            } else {
                System.out.println("## not exist command : " + json);
            }
        }
    }

    public static class Type1 {

        private String name;
        private BigInteger bi;
        private Integer type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigInteger getBi() {
            return bi;
        }

        @JsonSetter
        public void setBi(String bi) {
            if (bi != null) {
                if (bi.length() > 2 && bi.startsWith("0x")) {
                    bi = bi.substring(2);
                }

                this.bi = new BigInteger(bi, 16);
            }
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Type1{" + "name='" + name + '\'' + ", bi=" + bi + ", type=" + type + '}';
        }
    }

    public static class Type2 {

        List<String> id;
        String name;
        Integer age;

        public List<String> getId() {
            return id;
        }

        public void setId(List<String> id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Type2{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
        }
    }
}
