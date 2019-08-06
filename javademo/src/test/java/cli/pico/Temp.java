package cli.pico;

import java.io.File;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 *
 */
public class Temp {

    public static void main(String[] args) {
        Tar tar = new Tar();
        tar.create = true;

        CommandLine commandLine = new CommandLine(tar);
        System.out.println(commandLine.toString());
    }

    public static class Tar {

        @Option(names = "-c", description = "create a new archive")
        boolean create;

        @Option(names = {"-f", "--file"}, paramLabel = "ARCHIVE", description = "the archive file")
        File archive;

        @Parameters(paramLabel = "FILE", description = "one ore more files to archive")
        File[] files;

        @Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
        private boolean helpRequested = false;
    }
}
