package demo.generator;

/**
 * Maker of command line generator
 *
 * @GitHub : https://github.com/zacscoding
 */
public interface CommandLineGenerative {

    String DEFAULT_PREFIX = "";

    /**
     * Getting prefix for command generation
     */
    String getPrefix();

    /**
     * Getting description for usage() method
     */
    String getDescription();

    /**
     * Getting usage for usage() method
     */
    String getUsage();
}
