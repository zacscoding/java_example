package util;

import java.util.HashMap;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class HashMapTest {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    @Test
    public void test() {
        HashMap<Integer, Integer> map = new HashMap<>();
        IntStream.range(1, 20).forEach(i -> {
            map.put(i, i);
        });
    }

    @Test
    public void indexOfTest() {
        int len = 7;
        IntStream.range(1, 16).forEach(i -> {
            SimpleLogger.println("check :: {}  & {} == {}", i, len, (i & len));
            SimpleLogger.println(toBinaryStringFromByte((byte) i, 8));
            SimpleLogger.println(toBinaryStringFromByte((byte) len, 8));
            SimpleLogger.println(toBinaryStringFromByte((byte) (i & len), 8));
            SimpleLogger.println("=================================");
        });
    }

    @Test
    public void tableSizeFor() {
        IntStream.range(1, 33).forEach(i -> {
            int tableSize = tableSizeFor(i);
            int next = tableSize << 1;
            SimpleLogger.println("Capacity : {} , tableSizeFor : {}, next : {}", i, tableSize, next);
        });
    }

    @Test
    public void supplementHashFunc() {
        int repeat = 10;
        int start = Integer.MAX_VALUE - repeat - 1;
        for (int i = 0; i < repeat; i++) {
            start++;
            int shift = start >>> 16;
            int h = (start) ^ shift;
            SimpleLogger.println("============== check " + start + "   ==============");
            SimpleLogger.println("{} => {}", toBinaryStringFromInteger(start, 32), start);
            SimpleLogger.println("{} => {}", toBinaryStringFromInteger(shift, 32), shift);
            SimpleLogger.println("{} => {}", toBinaryStringFromInteger(h, 32), h);
            SimpleLogger.println("===========================================================");
        }
    }

    private int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    private String toBinaryStringFromInteger(int value, int bit) {
        String binaryString = Integer.toBinaryString(value);
        return adjustWithBit(binaryString, bit);
    }

    private String toBinaryStringFromByte(byte value, int bit) {
        String binaryString = Integer.toBinaryString(value);
        return adjustWithBit(binaryString, bit);
    }

    private String adjustWithBit(String binaryString, int bit) {
        int remain = bit - binaryString.length();
        if (bit > 0) {
            StringBuilder sb = new StringBuilder(bit);
            while (remain-- > 0) {
                sb.append(0);
            }
            sb.append(binaryString);
            return sb.toString();
        }

        return binaryString;
    }
}
