package json.jackson;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-05
 * @GitHub : https://github.com/zacscoding
 */
public class JsonAnnotationTest {

    @Test
    public void read() {
        String json = "{\"id\":1,\"name\":\"hivava\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonAnnotationTestEntity entity = objectMapper.readValue(json, JsonAnnotationTestEntity.class);

            assertTrue(entity.id == 1L);
            assertThat(entity.name, is("hivava"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read2() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        JsonAnnotationTestEntity2 temp1 = new JsonAnnotationTestEntity2(10, "hivava", 10.5);
        String json = mapper.writeValueAsString(temp1);
        assertThat(json, is("{\"customAge\":10,\"hiavaaName\":\"hivava\"}"));

        JsonAnnotationTestEntity2 read = mapper.readValue(json, JsonAnnotationTestEntity2.class);
        assertTrue(read.getScore() == 0D);

        String jsonValue = "{\"customAge\":10,\"hiavaaName\":\"hivava\", \"score\" : 2.2}";
        read = mapper.readValue(jsonValue, JsonAnnotationTestEntity2.class);
        assertTrue(read.getScore() == 2.2D);
    }


    @JsonInclude(Include.NON_EMPTY)
    private static class JsonAnnotationTestEntity {

        private final long id;
        private String name;

        @JsonCreator
        public JsonAnnotationTestEntity(@JsonProperty("id") long id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class JsonAnnotationTestEntity2 {

        private int age;
        private String name;
        private double score;

        public JsonAnnotationTestEntity2() {
        }

        public JsonAnnotationTestEntity2(int age, String name, double score) {
            this.age = age;
            this.name = name;
            this.score = score;
        }

        @JsonProperty("customAge")
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @JsonGetter("hiavaaName")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonIgnore
        public double getScore() {
            return score;
        }

        @JsonProperty
        public void setScore(double score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "JacksonDemoTempClazz{" + "age=" + age + ", name='" + name + '\'' + ", score=" + score + '}';
        }
    }
}