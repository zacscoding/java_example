package demo.basic;

import com.beust.jcommander.JCommander;
import demo.parameters.BasicParameter;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class JCommanderBasicTest {

    @Test
    public void basic() {
        BasicParameter basicParameter = new BasicParameter();
        String[] argv = {"-log", "2", "-groups", "unit1,unit2,unit3",
            "-debug", "-Doption=value", "a", "b", "c"};
        JCommander.newBuilder()
            .addObject(basicParameter)
            .build()
            .parse(argv);

        JCommander jCommander = JCommander.newBuilder()
            .addObject(basicParameter)
            .build();

        jCommander.usage();

        System.out.println(basicParameter);
    }
}
