package exec.apache;

/**
 * @author zacconding
 * @Date 2018-09-08
 * @GitHub : https://github.com/zacscoding
 */
public interface ApacheProcessExecutorHandler {

    void onStandardOutput(String line);

    void onStandardError(String line);
}