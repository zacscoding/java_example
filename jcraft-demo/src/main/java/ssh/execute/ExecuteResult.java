package ssh.execute;

/**
 * @author zacconding
 * @Date 2018-11-07
 * @GitHub : https://github.com/zacscoding
 */
public class ExecuteResult {

    private int exitValue;
    private String standardOutput;
    private String errorOutput;

    public ExecuteResult(int exitValue, String standardOutput, String errorOutput) {
        this.exitValue = exitValue;
        this.standardOutput = standardOutput;
        this.errorOutput = errorOutput;
    }

    public long getExitValue() {
        return exitValue;
    }

    public String getStandardOutput() {
        return standardOutput;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    @Override
    public String toString() {
        return "ExecuteResult{" + "\nexitValue=" + exitValue + "\nstandardOutput='" + standardOutput + "\nerrorOutput='" + errorOutput;
    }
}