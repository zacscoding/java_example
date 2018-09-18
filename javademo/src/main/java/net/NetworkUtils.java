package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * WORKING!
 *
 * @author zacconding
 * @Date 2018-09-13
 * @GitHub : https://github.com/zacscoding
 */
public class NetworkUtils {

    public static void main(String[] args) {
        args = new String[] {"-listen", "192.168.5.77", "23"};
        try {
            if (args == null || args.length == 0 || args[0].equals("-help")) {
                printHelp();
            } else if (args[0].equals("-listen")) {
                processListen(args);
            } else {
                printHelp();
            }
        } catch (InvalidArgsException e) {
            printHelp();
        }
    }

    private static void processListen(String[] args) throws InvalidArgsException {
        String host;
        int port;
        try {
            host = args[1];
            port = Integer.parseInt(args[2]);

            if (host == null || host.length() == 0) {
                throw new InvalidArgsException("args[1] must not be empty");
            }
        } catch (NumberFormatException e) {
            throw new InvalidArgsException("args[2] must be Number");
        } catch (Exception e) {
            System.err.println("Excpetion occur : " + e.getMessage());
            return;
        }

        SocketAddress socketAddress = new InetSocketAddress(host, port);
        Socket socket = new Socket();
        try {
            System.out.println(">> Check " + host + ":" + port);
            socket.connect(socketAddress, 2000);
            socket.close();
            System.out.println(">>> Success");
        } catch (Exception e) {
            System.err.println("Can`t reachable : " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void printHelp() {
        System.out.printf("%-30s -- help message\n", "-help");
        System.out.printf("%-30s -- check listen port\n", "-listen [host] [port]");
        System.out.println();
    }

    private static class InvalidArgsException extends Exception {

        public InvalidArgsException(String message) {
            super(message);
        }
    }
}