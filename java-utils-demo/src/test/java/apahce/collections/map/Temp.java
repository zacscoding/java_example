package apahce.collections.map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-12
 * @GitHub : https://github.com/zacscoding
 */
public class Temp {

    @Test
    public void temp() {
        Table<String, Long, String> table = HashBasedTable.create();
        table.put("0xaa", Long.valueOf(1L), "Block1");
        table.put("0xab", Long.valueOf(2L), "Block2");
        table.put("0xac", Long.valueOf(3L), "Block3");
    }
}
