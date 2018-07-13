package errors;

/**
 * @author zacconding
 * @Date 2018-07-07
 * @GitHub : https://github.com/zacscoding
 */
public class ResultWithError<R> {

    private R result;
    private Throwable error;

    public ResultWithError() {
    }

    public ResultWithError(R result) {
        this.result = result;
    }

    public ResultWithError(Throwable error) {
        this.error = error;
    }

    public boolean isError() {
        return error != null;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public R getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }
}