package trace.method;

import org.junit.Test;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class TransactionTraceTest {

    @Test
    public void traceCallStackSample() {

        TransactionTrace.startTransaction("ClassA::method1(String,int)");
        TransactionTrace.appendParam("param1");
        TransactionTrace.appendParam(Integer.valueOf(1));

        TransactionTrace.startTransaction("ClassB::method11(int)");
        TransactionTrace.appendParam(Integer.valueOf(2));
        TransactionTrace.appendReturnValue("returnValue~");

        TransactionTrace.startTransaction("ClassC::method112(double)");
        TransactionTrace.appendParam(Double.valueOf(2.2D));
        TransactionTrace.appendReturnValue("void");
        TransactionTrace.endTransaction();

        TransactionTrace.endTransaction();

        TransactionTrace.startTransaction("ClassD::method12()");
        TransactionTrace.appendReturnValue("void");
        TransactionTrace.endTransaction();

        TransactionTrace.appendReturnValue("return value~!");
        TransactionTrace.endTransaction();
    }
}
