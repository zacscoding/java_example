package demo.parameters;

import demo.generator.CommandLineGenerative;
import demo.generator.annotation.ArgOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateParameter implements CommandLineGenerative {

    @Builder.Default
    private String prefix = DEFAULT_PREFIX;

    @ArgOption(opt = "-i", desc = "this is integer arg")
    private Integer intValue;

    @ArgOption(opt = "-s", desc = "this is string arg", longOpt = "--string", required = true)
    private String stringValue;

    @ArgOption(opt = "-h", desc = "this is not primitive class")
    private GenerateParameterInner inner;

    @Override
    public String getPrefix() {
        if (prefix == null) {
            prefix = DEFAULT_PREFIX;
        }

        return prefix;
    }

    @Override
    public String getDescription() {
        return "This command is for test :)\n"
            + "this description is used in usage() method";
    }

    @Override
    public String getUsage() {
        return "test.sh [COMMAND] [OPTIONS]";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenerateParameterInner {

        private String ip;
        private String username;
        private int port;

        @Override
        public String toString() {
            return username + "@" + ip + ":" + port;
        }
    }
}
