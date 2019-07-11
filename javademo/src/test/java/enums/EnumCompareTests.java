package enums;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EnumCompareTests {

    @Test
    public void compareTo() {
        assertTrue(TestEnums.COMPLETE.compareTo(TestEnums.STARTED) < 0);
    }

    public enum TestEnums {
        COMPLETE, STARTING, STARTED, STOPPING, STOPPED;
    }
}
