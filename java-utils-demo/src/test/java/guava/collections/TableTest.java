package guava.collections;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-11-12
 * @GitHub : https://github.com/zacscoding
 */
public class TableTest {

    @Test
    public void hashBasedTableTest() {
        HashBasedTable<String, Long, String> table = HashBasedTable.create();

        table.put("0x1", 1L, "Block1");
        table.put("0x2", 2L, "Block2");
        table.put("0x3", 3L, "Block3");
        table.put("0x4", 4L, "Block4");

        System.out.println(table.row("0x1").size());
    }
}
