package trace.method;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionContext {

    private List<MethodContext> methods = new LinkedList<>();
    public Stack<MethodContext> callStack = new Stack<>();

    /**
     * Return traced method contexts
     */
    public List<MethodContext> getMethods() {
        return new ArrayList<>(methods);
    }

    public void appendParam(Object value) {
        if (callStack.isEmpty()) {
            System.err.println("appendParam(Object) called although has no trace method call");
            return;
        }

        callStack.peek().appendParam(value);
    }

    public void appendReturnValue(Object value, long endTime) {
        if (callStack.isEmpty()) {
            System.err.println("appendReturnValue(Object,long) called although has no trace method call");
            return;
        }

        callStack.peek().appendReturnValue(value, endTime);
    }

    /**
     * Start trace method call
     */
    public void startMethod(MethodContext methodContext) {
        if (methodContext == null) {
            System.err.println("received null method context");
            return;
        }

        methodContext.setDepth(callStack.size());
        callStack.push(methodContext);
        methods.add(methodContext);
    }

    /**
     * End trace method call
     */
    public void endMethod() {
        if (callStack.isEmpty()) {
            System.err.println("endMethod called with empty stack.");
            return;
        }

        callStack.pop();
    }

    public boolean hasTrace() {
        return !callStack.isEmpty();
    }
}
