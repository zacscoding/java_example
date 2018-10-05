package demo;

import java.util.Scanner;

/**
 * @author zacconding
 * @Date 2018-10-05
 * @GitHub : https://github.com/zacscoding
 */
public class Runner {

    public static Scanner scanner;

    public static void main(String[] args) {
        try {
            scanner = new Scanner(System.in);
            String rpcType = printAndGet("Rpc type [ipc/json/websocket] : ");
            System.out.println("Read rpc type : " + rpcType);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static String printAndGet(String content) {
        System.out.print(content + " : ");
        return scanner.next();
    }
}
