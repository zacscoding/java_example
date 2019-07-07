package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class PrintstreamTest {

    @Test
    public void compare() throws Exception {
        //PrintType printType = PrintType.PRINTLN_APPEND;
        PrintType printType = PrintType.PRINTLN_BUILDER_INIT_CAPACITY;
        // PrintType printType = PrintType.PRINTF;
        int repeat = 10000;
        int threadCount = 5;
        File targetFile = new File("src/test/resources/io.txt");
        if (targetFile.exists()) {
            targetFile.delete();
        }

        PrintStream ps = new PrintStream(new FileOutputStream(targetFile));
        String[] messages = new String[repeat];

        for (int i = 0; i < repeat; i++) {
            messages[i] = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        }

        Task[] tasks = new Task[threadCount];
        for (int i = 0; i < threadCount; i++) {
            tasks[i] = new Task(messages, ps, printType);
        }

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            tasks[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            tasks[i].join();
        }
        long elapsed = System.currentTimeMillis() - start;

        System.out.printf("## print type : %s >> %d [MS]\n", printType, elapsed);

        // Output
        // ## print type : PRINTLN_APPEND >> 336 [MS]
        // ## print type : PRINTLN_BUILDER >> 378 [MS]
        // ## print type : PRINTF >> 459 [MS]
    }

    @AllArgsConstructor
    public static class Task extends Thread {

        private String[] message;
        private PrintStream ps;
        private PrintType printType;

        @Override
        public void run() {
            int repeat = message.length - 1;
            for (int i = 0; i < repeat; i++) {
                switch (printType) {
                    case PRINTLN_APPEND:
                        ps.println(message[i] + message[i + 1]);
                        break;
                    case PRINTLN_BUILDER_INIT_CAPACITY:
                        ps.println(
                            new StringBuilder(message[i].length() + message[i + 1].length() + 50)
                                .append(message[i])
                                .append(message[i + 1])
                                .toString()
                        );
                        break;
                    case PRINTF:
                        ps.printf("%s%s\n", message[i], message[i + 1]);

                }
            }
        }
    }

    private enum PrintType {
        PRINTLN_APPEND,
        PRINTLN_BUILDER_INIT_CAPACITY,
        PRINTF;
    }
}

