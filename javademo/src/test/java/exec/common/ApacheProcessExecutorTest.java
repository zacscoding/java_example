package exec.common;

import static org.junit.Assert.assertTrue;

import exec.apache.ApacheProcessExecutor;
import exec.apache.ApacheProcessExecutorHandler;
import exec.apache.ApacheProcessExecutorImpl;
import exec.apache.ExecuteResult;
import java.io.IOException;
import org.junit.Test;

/**
 * ref https://www.programcreek.com/java-api-examples/?code=polygOnetic/guetzliconverter/guetzliconverter-master/src/guetzliconverter/ProcessExecutor.java#
 *
 * @author zacconding
 * @Date 2018-09-08
 * @GitHub : https://github.com/zacscoding
 */
public class ApacheProcessExecutorTest {

    @Test
    public void consoleTest() throws IOException {
        String command = getCommand(3);
        ApacheProcessExecutor executor = new ApacheProcessExecutorImpl();
        ExecuteResult result1 = executor.executeAndGetResult(command);
        System.out.println(result1);

        System.out.println("-----------------------------------------------------------------------------");
        ExecuteResult result2 = executor.executeAndGetResult(command, 2 * 1000);
        System.out.println(result2);

        System.out.println("-----------------------------------------------------------------------------");
        ExecuteResult result3 = executor.executeAndGetResult(command, 2 * 1000, new ApacheProcessExecutorHandler() {
            @Override
            public void onStandardOutput(String line) {
                System.out.println(line);
            }

            @Override
            public void onStandardError(String line) {
                System.err.println(line);
            }
        });
    }

    @Test
    public void execute() throws IOException {
        String command = getCommand(2);
        ApacheProcessExecutor executor = new ApacheProcessExecutorImpl();
        assertTrue(executor.execute(command) > -1);
        System.out.println(">> Success assertTrue(executor.execute(command) > -1");

        assertTrue(executor.execute(command, 1 * 1000) == ApacheProcessExecutor.WATCHDOG_EXIST_VALUE);
        System.out.println(">> Success executor.execute(command, 1 * 1000) == ApacheProcessExecutor.WATCHDOG_EXIST_VALUE");

        long exitValue = executor.execute(command, 3 * 1000, new ApacheProcessExecutorHandler() {
            @Override
            public void onStandardOutput(String line) {
                System.out.println(line);
            }

            @Override
            public void onStandardError(String line) {
                System.err.println(line);
            }
        });
        assertTrue(exitValue > -1);
    }

    private String getCommand(int sleep) {
        return "java Temp " + sleep;
    }
}
