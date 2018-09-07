package exec.apache;

import java.io.IOException;

/**
 * @author zacconding
 * @Date 2018-09-07
 * @GitHub : https://github.com/zacscoding
 */
public interface ApacheProcessExecutor {
    long WATCHDOG_EXIST_VALUE = -999L;

    long execute(String command) throws IOException;

    long execute(String command, long timeout) throws IOException;

    long execute(String command, long timeout, ApacheProcessExecutorHandler executorHandler) throws IOException;

    ExecuteResult executeAndGetResult(String command) throws IOException;

    ExecuteResult executeAndGetResult(String command, long timeout) throws IOException;

    ExecuteResult executeAndGetResult(String command, long timeout, ApacheProcessExecutorHandler executorHandler) throws IOException;
}