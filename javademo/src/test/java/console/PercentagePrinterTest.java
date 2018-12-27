package console;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class PercentagePrinterTest {

    public static void main(String[] args) throws Exception {
        PercentagePrinter printer = new PercentagePrinter();

        for (double progressPercentage = 0.0D; progressPercentage <= 1.0D; progressPercentage += 0.01D) {
            printer.updateProgress(progressPercentage);
            Thread.sleep(20);
        }
        printer.complete();
    }
}
