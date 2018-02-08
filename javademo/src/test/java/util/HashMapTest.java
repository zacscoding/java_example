package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2018-01-14
 * @GitHub : https://github.com/zacscoding
 */
public class HashMapTest {
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    @Test
    public void test() {
        for(int i=1; i<22; i++) {
            testHashMap(1 << i);
            // testArrayList(1 << i);
        }
    }

    /* ================================ Result ================================
    ## data : 2     // default : 16236ns    // initialized : 2643ns // diff : 13593ns, 0ms
    ## data : 4     // default : 4908ns     // initialized : 1133ns // diff : 3775ns, 0ms
    ## data : 8     // default : 2643ns     // initialized : 755ns // diff : 1888ns, 0ms
    ## data : 16    // default : 18879ns    // initialized : 755ns // diff : 18124ns, 0ms
    ## data : 32    // default : 7929ns     // initialized : 1511ns // diff : 6418ns, 0ms
    ## data : 64    // default : 15859ns    // initialized : 2266ns // diff : 13593ns, 0ms
    ## data : 128   // default : 20389ns    // initialized : 4531ns // diff : 15858ns, 0ms
    ## data : 256   // default : 37381ns    // initialized : 8306ns // diff : 29075ns, 0ms
    ## data : 512   // default : 53616ns    // initialized : 15858ns // diff : 37758ns, 0ms
    ## data : 1024  // default : 94773ns    // initialized : 35492ns // diff : 59281ns, 0ms
    ## data : 2048  // default : 496142ns   // initialized : 116673ns // diff : 379469ns, 0ms
    ## data : 4096  // default : 303953ns   // initialized : 101192ns // diff : 202761ns, 0ms
    ## data : 8192  // default : 267705ns   // initialized : 200496ns // diff : 67209ns, 0ms
    ## data : 16384  // default : 185769ns  // initialized : 143859ns // diff : 41910ns, 0ms
    ## data : 32768  // default : 1546572ns // initialized : 1069688ns // diff : 476884ns, 0ms
    ## data : 65536  // default : 2074054ns // initialized : 2010997ns // diff : 63057ns, 0ms
    ## data : 131072 // default : 6081700ns // initialized : 4083540ns // diff : 1998160ns, 1ms
    ## data : 262144 // default : 2095953ns // initialized : 1103670ns // diff : 992283ns, 0ms
    ## data : 524288 // default : 3597216ns // initialized : 2418785ns // diff : 1178431ns, 1ms
    ## data : 1048576 // default : 8077972ns // initialized : 4623859ns // diff : 3454113ns, 3ms
    ## data : 2097152 // default : 15593349ns// initialized : 9846185ns // diff : 5747164ns, 5ms
    ========================================================================================== */
    public void testArrayList(int dataCnt) {
        String[] randoms = new String[dataCnt];
        for(int i=0; i<dataCnt; i++) {
            randoms[i] = UUID.randomUUID().toString();
        }

        long[] times = new long[2];

        long startTime = -System.nanoTime();
        List<String> defaultList = new ArrayList<>();
        for(int i=0; i<dataCnt; i++) {
            defaultList.add(randoms[i]);
        }
        long endTime = System.nanoTime();
        times[0] = endTime + startTime;

        startTime = -System.nanoTime();
        List<String> initializedList = new ArrayList<>(dataCnt);
        for(int i=0; i<dataCnt; i++) {
            initializedList.add(randoms[i]);
        }
        endTime = System.nanoTime();
        times[1] = endTime + startTime;
        long diff = times[0] - times[1];
        SimpleLogger.println("## data : {} // default : {}ns // initialized : {}ns // diff : {}ns, {}ms", dataCnt, times[0], times[1], diff, TimeUnit.NANOSECONDS.toMillis(diff));
    }

    /* ========================== RESULT ==========================
## data : 2 // default : 13216ns // initialized : 6419ns // diff : 6797ns, 0ms
## data : 4 // default : 3021ns // initialized : 2644ns // diff : 377ns, 0ms
## data : 8 // default : 2265ns // initialized : 1888ns // diff : 377ns, 0ms
## data : 16 // default : 9062ns // initialized : 3398ns // diff : 5664ns, 0ms
## data : 32 // default : 9062ns // initialized : 3775ns // diff : 5287ns, 0ms
## data : 64 // default : 18879ns // initialized : 10572ns // diff : 8307ns, 0ms
## data : 128 // default : 30962ns // initialized : 11706ns // diff : 19256ns, 0ms
## data : 256 // default : 59658ns // initialized : 20767ns // diff : 38891ns, 0ms
## data : 512 // default : 106855ns // initialized : 40023ns // diff : 66832ns, 0ms
## data : 1024 // default : 209935ns // initialized : 83823ns // diff : 126112ns, 0ms
## data : 2048 // default : 500673ns // initialized : 411941ns // diff : 88732ns, 0ms
## data : 4096 // default : 1803705ns // initialized : 467445ns // diff : 1336260ns, 1ms
## data : 8192 // default : 6128520ns // initialized : 1003989ns // diff : 5124531ns, 5ms
## data : 16384 // default : 3671599ns // initialized : 1396296ns // diff : 2275303ns, 2ms
## data : 32768 // default : 6673747ns // initialized : 2112190ns // diff : 4561557ns, 4ms
## data : 65536 // default : 12603660ns // initialized : 7319034ns // diff : 5284626ns, 5ms
## data : 131072 // default : 35754674ns // initialized : 17453690ns // diff : 18300984ns, 18ms
## data : 262144 // default : 48905449ns // initialized : 25150305ns // diff : 23755144ns, 23ms
## data : 524288 // default : 101153247ns // initialized : 36348231ns // diff : 64805016ns, 64ms
## data : 1048576 // default : 264154292ns // initialized : 100401105ns // diff : 163753187ns, 163ms
## data : 2097152 // default : 525818058ns // initialized : 147244582ns // diff : 378573476ns, 378ms
    ============================================================== */
    public void testHashMap(int dataCnt) {
        String[] randoms = new String[dataCnt];
        for(int i=0; i<dataCnt; i++) {
            randoms[i] = UUID.randomUUID().toString();
        }

        long[] times = new long[2];

        long startTime = -System.nanoTime();
        Map<String,String> defaultMap = new HashMap<>();
        for(int i=0; i<dataCnt; i++) {
            defaultMap.put(randoms[i], randoms[i]);
        }
        long endTime = System.nanoTime();
        times[0] = endTime + startTime;

        startTime = -System.nanoTime();
        Map<String,String> capacityMap = new HashMap<>(initializeCapacity(dataCnt));
        for(int i=0; i<dataCnt; i++) {
            capacityMap.put(randoms[i], randoms[i]);
        }
        endTime = System.nanoTime();

        times[1] = endTime + startTime;
        long diff = times[0] - times[1];
        SimpleLogger.println("## data : {} // default : {}ns // initialized : {}ns // diff : {}ns, {}ms", dataCnt, times[0], times[1], diff, TimeUnit.NANOSECONDS.toMillis(diff));
    }

    public int initializeCapacity(int expectedMaximalNumber) {
        return (int)(expectedMaximalNumber/0.75+1);
    }

    public static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
