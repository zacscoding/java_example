package errors;

/**
 * @author zacconding
 * @Date 2018-07-07
 * @GitHub : https://github.com/zacscoding
 */
public class ResultWithErrorSample {

    public static ResultWithError<Integer> parseInt(String value) {
        try {
            return new ResultWithError<>(Integer.parseInt(value));
        } catch (Throwable t) {
            return new ResultWithError<>(t);
        }
    }
}
