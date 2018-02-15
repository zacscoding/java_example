package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-02-13
 * @GitHub : https://github.com/zacscoding
 */
public class GsonTest {

    @Test
    public void test() {
        GsonTestDomain domain = new GsonTestDomain(1, ";\\", "\"\t");
        String json = new GsonBuilder().create().toJson(domain).replace("\\\\", "\\\\\\\\");
        System.out.println(json);
    }

    @Test
    public void test2() {
        String[] tests = {"meow\\meow", "meow \\ ", "meow \\ ", "meow \\\"meow\\\""};
        for (String s : tests) {
            GsonTestDoamin2 test = new GsonTestDoamin2();
            test.value = s;
            System.out.println(test.value);
            String json = new Gson().toJson(test);
            System.out.println(json);
            GsonTestDoamin2 test2 = new Gson().fromJson(json, GsonTestDoamin2.class);
            System.out.println(test2.value);
            if (!test2.value.equals(test.value)) {
                throw new RuntimeException(s);
            }
            System.out.println();
        }
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
class GsonTestDomain {

    int age;
    String hobby;
    String job;
}

@ToString
class GsonTestDoamin2 {

    public String value;
}