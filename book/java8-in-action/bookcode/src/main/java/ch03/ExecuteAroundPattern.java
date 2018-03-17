package ch03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * == Init/preparation code ==
 *
 * == Task A, B, C .. ==
 *
 * == Cleanup/finishing code ==
 */
public class ExecuteAroundPattern {

    public static String readFirstLine() {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            // Task A, Task B , Task C
            return br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Step 3 : Execute a behavior
     */
    public static String processFile(BufferedReaderProcessor processor) throws IOException {
        ClassLoader classLoader = ExecuteAroundPattern.class.getClassLoader();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(classLoader.getResource("data.txt").getFile())))) {
            return processor.process(br);
        }
    }

    public static void main(String[] args) throws IOException {
        /**
         * Stemp 4 : Pass lambdas
         */
        String oneLine = processFile((BufferedReader br) -> br.readLine());
        String twoLine = processFile((BufferedReader br) -> br.readLine() + " " + br.readLine());
        System.out.println(oneLine);
        System.out.println(twoLine);
    }


}
