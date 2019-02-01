package demo;

import java.util.concurrent.TimeUnit;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class DockerApiMain {
    public static void main(String[] args) throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            TimeUnit.SECONDS.sleep(5L);
        }
    }
}
