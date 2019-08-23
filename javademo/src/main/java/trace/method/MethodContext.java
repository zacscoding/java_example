package trace.method;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class MethodContext {

    // classname::methodname
    private String id;
    // start time
    private long startTime;
    // end time
    private long endTime;
    // parameters
    private List<String> params = new ArrayList<>();
    // return value
    private String returnValue;
    // exception
    private Exception exception;
    // depth in call stack
    private int depth;

    public MethodContext(String id, long startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public void appendParam(Object obj) {
        params.add(obj == null ? "null" : obj.toString());
    }

    public void appendReturnValue(Object obj, long endTime) {
        this.returnValue = obj == null ? "null" : obj.toString();
        this.endTime = endTime;
    }

    // getters, setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
