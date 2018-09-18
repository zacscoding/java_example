package json;

import util.GsonUtil;

/**
 * @author zacconding
 * @Date 2018-09-18
 * @GitHub : https://github.com/zacscoding
 */
public class JsonTestEntity {

    private String name;
    private JsonTestEnum jsonTestEnum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonTestEnum getJsonTestEnum() {
        return jsonTestEnum;
    }

    public void setJsonTestEnum(int value) {
        this.jsonTestEnum = JsonTestEnum.getTypeByValue(value);
    }

    public void setJsonTestEnum(JsonTestEnum jsonTestEnum) {
        this.jsonTestEnum = jsonTestEnum;
    }

    @Override
    public String toString() {
        return GsonUtil.toString(this);
    }
}
