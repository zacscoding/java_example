package util;

import org.junit.Test;

/**
 * ref :: http://fendee.egloos.com/9022534
 *
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
public class BitTest {

    private final int DISPLAY_BIT = 8;
    private final int INT_TO_BIT = 32;

    @Test
    public void print() {
        int a = 160, b = 245;
        System.out.println("================ 논리곱 AND(&) ================");
        System.out.println("각 비트 b1 and b2 가 1이면 1, 아니면 0");
        System.out.println(String.format("a = %d, b=%d ==> a&b", a, b));
        System.out.println(toBinaryString(a));
        System.out.println(toBinaryString(b));
        System.out.println(toBinaryString(a & b));

        System.out.println("================ 논리합 OR(|) ================");
        System.out.println("각 비트 b1 or b2가 1이면 1, 아니면 0");
        System.out.println(String.format("a = %d, b=%d ==> a|b", a, b));
        System.out.println(toBinaryString(a));
        System.out.println(toBinaryString(b));
        System.out.println(toBinaryString(a | b));

        System.out.println("================ 베타적 논리합 XOR(|) ================");
        System.out.println("각 비트가 다르면 1, 아니면 0");
        System.out.println(String.format("a = %d, b=%d ==> a^b", a, b));
        System.out.println(toBinaryString(a));
        System.out.println(toBinaryString(b));
        System.out.println(toBinaryString(a ^ b));

        System.out.println("================ 1의 보수 표현 NOT (~) ================");
        System.out.println("각 비트를 반전시킨 값");
        System.out.println(String.format("a = %d ==> ~a", a));
        System.out.println(toBinaryString(a, 32));
        System.out.println(toBinaryString(~a));

        a = 178;
        int shift = 2;
        System.out.println("================ 왼쪽 쉬프트 연산자 (<<) ================");
        System.out.println(String.format("%d >> %d", a, shift));
        System.out.println(String.format("%d 값을 왼쪽으로 %d비트 시프트", a, shift));
        System.out.println(toBinaryString(a, DISPLAY_BIT + shift));
        System.out.println(toBinaryString((a << shift), DISPLAY_BIT + shift));

        a = Integer.MIN_VALUE;
        System.out.println("================ 오른쪽 쉬프트 연산자 (>>) ================");
        System.out.println(String.format("%d >> %d", a, shift));
        System.out.println(String.format("%d 값을 오른쪽으로 %d비트 시프트", a, shift));
        System.out.println("왼쪽 빈 값은 a값의 최초 첫째자리 값과 동일 한 값으로 채워짐");
        System.out.println(toBinaryString(a, INT_TO_BIT));
        System.out.println(toBinaryString((a >> shift), 32));

        System.out.println("================ 논리 오른쪽 시프트 연산자 (>>>) ================");
        System.out.println(String.format("%d >> %d", a, shift));
        System.out.println(String.format("%d 값을 오른쪽으로 %d비트 시프트 + 앞쪽 비트를 무조건 0으로", a, shift));
        System.out.println(toBinaryString(a, INT_TO_BIT));
        System.out.println(toBinaryString((a >>> shift), INT_TO_BIT));
    }

    private String toBinaryString(int value) {
        return toBinaryString(value, DISPLAY_BIT);
    }

    private String toBinaryString(int value, int bit) {
        String binaryString = Integer.toBinaryString(value);
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

    private String toTwoDigits(int val) {
        return String.format("%02d", val);
    }

}
