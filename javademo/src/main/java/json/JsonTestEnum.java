package json;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author zacconding
 * @Date 2018-09-18
 * @GitHub : https://github.com/zacscoding
 */
public enum JsonTestEnum {

    TEST1(1), TEST2(2);

    int value;

    JsonTestEnum(int value) {
        this.value = value;
    }

    private static final Set<JsonTestEnum> TYPES = EnumSet.allOf(JsonTestEnum.class);

    public static JsonTestEnum getTypeByValue(int value) {
        switch (value) {
            case 1:
                return TEST1;
            case 2:
                return TEST2;
            default:
                return null;
        }
    }

    public static JsonTestEnum getTypeByName(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }

        for (JsonTestEnum type : TYPES) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }
}
