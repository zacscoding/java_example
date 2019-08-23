package trace.method;

import java.util.List;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionTrace {

    public static void startTransaction(String id) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("Cannot create a TransactionContext");
            return;
        }

        MethodContext methodCtx = new MethodContext(id, System.currentTimeMillis());
        ctx.startMethod(methodCtx);
    }

    public static void appendParam(Object value) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("appendParam(Object) is called although not exit tx ctx");
            return;
        }

        ctx.appendParam(value);
    }

    public static void appendReturnValue(Object value) {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("appendReturnValue(Object) is called although not exit tx ctx");
            return;
        }

        ctx.appendReturnValue(value, System.currentTimeMillis());
    }

    public static void endTransaction() {
        TransactionContext ctx = TransactionContextManager.getOrCreateContext();

        if (ctx == null) {
            System.err.println("endTransaction() is called although not exist TransactionContext");
            return;
        }

        ctx.endMethod();

        if (ctx.hasTrace()) {
            return;
        }

        displayTracedCallStack(ctx);
    }

    private static void displayTracedCallStack(TransactionContext ctx) {
        synchronized (System.out) {
            List<MethodContext> methods = ctx.getMethods();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < methods.size(); i++) {
                MethodContext methodCtx = methods.get(i);

                // append depth
                int depth = methodCtx.getDepth();
                String depthPrefix = "";

                while (depth-- > 0) {
                    depthPrefix += "|  ";
                }

                sb.append(depthPrefix)
                  .append("+--")
                  .append(methodCtx.getId())
                  .append('[')
                  .append(methodCtx.getEndTime() - methodCtx.getStartTime())
                  .append("ms] : ")
                  .append(methodCtx.getReturnValue())
                  .append('\n');

                List<String> params = methodCtx.getParams();

                /*if (i == 0 && !params.isEmpty()) {
                    depthPrefix += "|  ";
                }*/

                for (int j = 0; j < params.size(); j++) {
                    sb.append(depthPrefix)
                      .append(" -- ")
                      .append(j + 1)
                      .append(" : ")
                      .append(params.get(j))
                      .append('\n');
                }
            }

            System.out.println(sb);
        }
    }
}
