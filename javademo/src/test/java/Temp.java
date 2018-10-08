import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-10-08
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    @Test
    public void temp() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "aa");
        System.out.println(map.put("1", "bb"));
        System.out.println(map.get("1"));
    }
}
