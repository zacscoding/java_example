package apahce.collections.util;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import util.DemoTask;
import util.GsonUtil;
import util.SimpleLogger;

/**
 * ref :: https://www.tutorialspoint.com/commons_collections/
 */
public class CollectionUtilTest {

    @Test
    public void total() {
        DemoTask.doTask("addIgnoreNull", this::addIgnoreNull);
        DemoTask.doTask("collate", this::collate);
    }


    private void addIgnoreNull() {
        List<String> list = new LinkedList<>();

        CollectionUtils.addIgnoreNull(list, null);
        assertTrue(list.size() == 0);

        CollectionUtils.addIgnoreNull(list, "zaccoding");
        assertTrue(list.size() == 1);
    }

    private void collate() {
        List<Integer> list1 = Arrays.asList(1, 3, 4);
        List<Integer> list2 = Arrays.asList(3, 2);

        List<Integer> mergedList1 = CollectionUtils.collate(list1, list2);
        List<Integer> mergedList2 = CollectionUtils.collate(list1, list2, false);

        SimpleLogger.build()
                    .appendln("list1 : " + GsonUtil.toString(list1))
                    .appendln("list2 : " + GsonUtil.toString(list2))
                    .appendln("merged1 : " + GsonUtil.toString(mergedList1))
                    .appendln("merged2 : " + GsonUtil.toString(mergedList2))
                    .flush();

        assertTrue(mergedList1.size() == 5);
        assertTrue(mergedList2.size() == 4);
    }
}