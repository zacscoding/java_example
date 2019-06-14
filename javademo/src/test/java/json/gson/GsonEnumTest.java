package json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import json.JsonTestEntity;
import json.JsonTestEnum;
import org.junit.Test;
import util.GsonUtil;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-09-18
 * @GitHub : https://github.com/zacscoding
 */
public class GsonEnumTest {

    @Test
    public void toJson() {
        Gson gson = GsonUtil.GsonFactory.createDefaultGson();

        JsonTestEntity entity = new JsonTestEntity();
        entity.setName("JsonTest");
        entity.setJsonTestEnum(JsonTestEnum.TEST1);

        System.out.println(gson.toJson(entity));
    }

    @Test
    public void readJsonWithDefault() {
        deserialize(GsonUtil.GsonFactory.createDefaultGson());
    }

    @Test
    public void readJsonWithAdapter() {
        GsonBuilder builder = GsonUtil.GsonFactory.createDefaultGsonBuilder();
        builder.registerTypeAdapter(JsonTestEnum.class, new JsonDeserializer<JsonTestEnum>() {
            @Override
            public JsonTestEnum deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                String stringValue = jsonElement.getAsString();
                try {
                    return JsonTestEnum.getTypeByValue(Integer.parseInt(stringValue));
                } catch (NumberFormatException e) {
                    return JsonTestEnum.getTypeByName(stringValue);
                }
            }
        });

        deserialize(builder.create());
    }

    public void deserialize(Gson gson) {
        List<String> jsonValues = Arrays
            .asList("{\"name\":\"JsonTest1\",\"jsonTestEnum\":\"TEST1\"}", "{\"name\":\"JsonTest2\",\"jsonTestEnum\":\"test1\"}", "{\"name\":\"JsonTest3\",\"jsonTestEnum\":\"1\"}",
                "{\"name\":\"JsonTest4\",\"jsonTestEnum\":1}");

        for (String jsonValue : jsonValues) {
            JsonTestEntity entity = gson.fromJson(jsonValue, JsonTestEntity.class);
            SimpleLogger.println("Parse : {}\n>> name : {} | enum : {}", jsonValue, entity.getName(), entity.getJsonTestEnum());
        }
    }

    @Test
    public void writeWithDefault() {
        serialize(GsonUtil.GsonFactory.createDefaultGson());
    }

    @Test
    public void writeWithAdapter() {
        GsonBuilder builder = GsonUtil.GsonFactory.createDefaultGsonBuilder();
        builder.registerTypeAdapter(JsonTestEntity.class, new JsonSerializer<JsonTestEntity>() {
            @Override
            public JsonElement serialize(JsonTestEntity jsonTestEntity, Type type, JsonSerializationContext jsonSerializationContext) {
                if (jsonTestEntity == null) {
                    return jsonSerializationContext.serialize(null);
                } else {
                    return jsonSerializationContext.serialize(jsonTestEntity.toString().toLowerCase(), String.class);
                }
            }
        });

        serialize(builder.create());
    }

    public void serialize(Gson gson) {
        List<JsonTestEntity> entities = Arrays.asList(new JsonTestEntity(), new JsonTestEntity(), new JsonTestEntity());

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).setName("JsonTest" + i);
            entities.get(i).setJsonTestEnum(i);
        }

        for (JsonTestEntity entity : entities) {
            System.out.println(gson.toJson(entity));
        }
    }
}
