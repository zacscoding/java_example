package console;

import java.io.PrintStream;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class PercentagePrinter {

    private final int width = 57;
    private PrintStream out;
    private String afterCompleteMessage;

    public PercentagePrinter() {
        this(System.out, "Complete");
    }

    public PercentagePrinter(PrintStream out, String afterCompleteMessage) {
        this.out = out;
        this.afterCompleteMessage = afterCompleteMessage;
    }

    public void updateProgress(double progressPercentage) {
        if (progressPercentage < 0.0) {
            return;
        }

        if (progressPercentage >= 1.0D) {
            complete();
            return;
        }

        out.print("\r[");

        int offset = 0;
        int size = (int) (progressPercentage * width);

        while (offset <= size) {
            out.print('#');
            offset++;
        }

        while (offset < width) {
            out.print(" ");
            offset++;
        }

        String percentage = String.format("%.2f%%", (progressPercentage * 100));
        out.printf("]  %3s", percentage);
    }

    public void complete() {
        out.println("\r" + afterCompleteMessage);
    }
}
