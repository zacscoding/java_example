package console;

import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public class PercentagePrinterTest {

    public static void main(String[] args) throws Exception {
        test2();
    }

    private static void test2() throws Exception {
        List<Double> percentages = Files.readAllLines(new ClassPathResource("common/percentage.txt").getFile().toPath())
            .stream()
            .filter(s -> s != null && s.length() > 0)
            .map(s -> Double.parseDouble(s))
            .collect(Collectors.toList());

        PercentagePrinter printer = new PercentagePrinter();

        for (double percentage : percentages) {
            printer.updateProgress(percentage);
            Thread.sleep(2L);
        }
    }

    private static void test1() throws Exception {
        PercentagePrinter printer = new PercentagePrinter();

        for (double progressPercentage = 0.0D; progressPercentage <= 1.0D; progressPercentage += 0.01D) {
            printer.updateProgress(progressPercentage);
            Thread.sleep(20);
        }

        printer.complete();
    }
}
