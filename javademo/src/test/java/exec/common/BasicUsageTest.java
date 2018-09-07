package exec.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-09-07
 * @GitHub : https://github.com/zacscoding
 */
public class BasicUsageTest {

    @Test
    public void defaultExecute() throws IOException {
        CommandLine cmdLine = new CommandLine("java");
        cmdLine.addArgument("Temp");
        cmdLine.addArgument("2");

        DefaultExecutor executor = new DefaultExecutor();

        ExecuteWatchdog watchDog = new ExecuteWatchdog(3 * 1000);
        executor.setWatchdog(watchDog);

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(stdout, stderr);
        executor.setStreamHandler(pumpStreamHandler);

        int result = executor.execute(cmdLine);

        SimpleLogger.build()
                    .appendln("result : " + result)
                    .appendln("stdout : " + stdout.toString())
                    .appendln("stderr : " + stderr.toString())
                    .flush();
    }

    @Test
    public void temp() throws Exception {
        Process p = Runtime.getRuntime().exec("java Temp 1");
        p.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String readLine;
        while ((readLine = br.readLine()) != null) {
            System.out.println(readLine);
        }
        System.out.println("------------------------------");
        br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((readLine = br.readLine()) != null) {
            System.out.println(readLine);
        }
        System.out.println("------------------------------");
    }
}