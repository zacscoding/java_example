package demo.generator;

/**
 * Command line generator interface
 *
 * @GitHub : https://github.com/zacscoding
 */
public interface CommandGenerator {

    /**
     * Generate command line
     */
    String generateCommandLine() throws IllegalAccessException;

    /**
     * Getting usage
     */
    String usage();
}
