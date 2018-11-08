package cli;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author zacconding
 * @Date 2018-11-08
 * @GitHub : https://github.com/zacscoding
 */
public class Main {
    private static final Options OPTIONS = generateOptions();

    public static void main(String[] args) {
        printUsage(OPTIONS);
        printHelp(OPTIONS);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String readLine = scanner.nextLine();
            if ("exit".equals(readLine)) {
                break;
            }

            String[] arguments = readLine.substring(5).split("\\s");
            try {
                CommandLine commandLine = generateCommandLine(OPTIONS, arguments);
                Option[] options = commandLine.getOptions();
                for (Option option : options) {
                    System.out.println(option);
                    System.out.println(String.format("Option : %s | value : %s", option.getOpt(), option.getValue()));
                }
            } catch (ParseException e) {
                // ignore
            }
        }
    }

    private static CommandLine generateCommandLine(final Options options, final String[] commandLineArguments) throws ParseException {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;

        try {
            commandLine = cmdLineParser.parse(options, commandLineArguments);
        } catch (ParseException parseException) {
            System.err.println(("ERROR: Unable to parse command-line arguments " + Arrays.toString(commandLineArguments) + " due to: " + parseException.getMessage()));
            throw parseException;
        }

        return commandLine;
    }

    private static Options generateOptions() {
        final Option helpOption = Option.builder("h").required(false).hasArg(false).longOpt("help").desc("Display help messages").build();

        final Option verboseOption = Option.builder("v")
                                           .required(false)
                                           .hasArg(false)
                                           .longOpt("VERBOSE_OPTION")
                                           .desc("Print status with verbosity.").build();

        final Option fileOption = Option.builder("f")
                                        .required()
                                        .longOpt("FILE_OPTION")
                                        .hasArg()
                                        .desc("File to be processed.")
                                        .build();

        return new Options().addOption(verboseOption).addOption(fileOption).addOption(helpOption);
    }

    private static void printUsage(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "Main";
        System.out.println("\n=====");
        System.out.println("USAGE");
        System.out.println("=====");
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 80, syntax, options);
        pw.flush();
    }

    private static void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        final String syntax = "Main";
        final String usageHeader = "Example of Using Apache Commons CLI";
        final String usageFooter = "See http://marxsoftware.blogspot.com/ for further details.";
        System.out.println("\n====");
        System.out.println("HELP");
        System.out.println("====");
        formatter.printHelp(syntax, usageHeader, options, usageFooter);
    }
}
