package io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author zacconding
 * @Date 2019-01-04
 * @GitHub : https://github.com/zacscoding
 */
public class PipedTest {

    /*
    --------------       --------------     in  --------------       --------------      --------------
    [ Piped Out  ]  ---  [  Piped IN  ]   ---> [ToUpperCaseTask] -->  [  Piped Out  ] --> [ Piped In ]
      poutWrapper           pin                                          pout                pinWrapper
    --------------       --------------         --------------  out  --------------      --------------
    */
    @Test
    public void readAndWrite() throws IOException, InterruptedException {
        ToUpperCaseTask task = new ToUpperCaseTask();

        // for task`s inputstream i.e write
        PipedInputStream pin = new PipedInputStream(32);
        PipedOutputStream poutWrapper = new PipedOutputStream(pin);

        // for task`s outputstream i.e read
        PipedInputStream pinWrapper = new PipedInputStream(32);
        PipedOutputStream pout = new PipedOutputStream(pinWrapper);

        // setup stream to Task
        task.setInputStream(pin);
        task.setOutputStream(pout);
        task.start();

        Thread readTask = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (pinWrapper.available() != 0) {

                        StringBuilder sb = new StringBuilder();

                        while (pinWrapper.available() != 0) {
                            sb.append((char) pinWrapper.read());
                        }

                        System.out.print(sb.toString());
                    }

                    Thread.sleep(100L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        readTask.setDaemon(true);
        readTask.start();

        poutWrapper.write("test\n".getBytes());
        TimeUnit.SECONDS.sleep(1L);

        poutWrapper.write("pipe stream :::::(()))\n".getBytes());
        TimeUnit.SECONDS.sleep(5L);

        // Ooutput
//      TEST
//      PIPE STREAM :::::(()))
    }

    private static class ToUpperCaseTask {

        InputStream inputStream;
        OutputStream outputStream;

        public void start() {
            Thread task = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (inputStream.available() != 0) {
                            char read = Character.toUpperCase((char) inputStream.read());
                            outputStream.write(read);
                        }

                        Thread.sleep(100L);
                    }
                } catch (Exception e) {

                }
            });

            task.setDaemon(true);
            task.start();
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public OutputStream getOutputStream() {
            return outputStream;
        }

        public void setOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }
    }
}
