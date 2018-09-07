package exec.apache;

/**
 * @author zacconding
 * @Date 2018-09-08
 * @GitHub : https://github.com/zacscoding
 */
public class ExecuteResult {

    private long exitValue;
    private String standardOutput;
    private String errorOutput;

    public ExecuteResult(long exitValue, String standardOutput, String errorOutput) {
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
        return "ExecuteResult{"
            + "\nexitValue=" + exitValue
            + "\nstandardOutput='" + standardOutput
            + "\nerrorOutput='" + errorOutput;
    }
}