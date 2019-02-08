package demo;

import java.util.concurrent.TimeUnit;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class Main {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            System.out.print("\r\r\r\r\r");
            StringBuilder sb = new StringBuilder();
            sb.append("Work...").append(i).append("\n");
            for (int j = 0; j < 3; j++) {
                sb.append("Line.......").append(j).append("\n");
            }
            System.out.println(sb.toString());
            TimeUnit.SECONDS.sleep(1L);
        }
    }

}
