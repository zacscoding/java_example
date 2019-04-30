package cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class WriteDemo {

    public static void main(String[] args) throws Exception {
        Options options = generateOptions();
        String[] values = {
            "-n", "hivava", "-a", "18"
        };
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine parse = cmdLineParser.parse(options, values);
    }

    private static Options generateOptions() {
        final Option nameOption = Option.builder("n")
            .required(false).hasArg(true)
            .longOpt("name")
            .desc("this is name")
            .build();

        final Option ageOption = Option.builder("a")
            .required(true)
            .hasArg(true)
            .longOpt("age")
            .desc("this is age")
            .build();

        return new Options().addOption(nameOption).addOption(ageOption);
    }
}
