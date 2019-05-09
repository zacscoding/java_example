package demo.parameters;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import demo.generator.CommandLineGenerative;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * https://github.com/cbeust/jcommander
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BasicParameter implements CommandLineGenerative {

    @Parameter
    private List<String> parameters = Lists.newArrayList();

    @Parameter(names = {"-log", "-verbose"}, description = "Level of verbosity")
    public Integer verbose = 1;

    @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
    public String groups;

    @Parameter(names = "-debug", description = "Debug mode")
    public boolean debug = false;

    @DynamicParameter(names = "-D", description = "Dynamic parameters go here")
    public Map<String, String> dynamicParams = new HashMap<>();

    @Builder.Default
    private String prefix = "";

    @Override
    public String getPrefix() {
        if (prefix == null) {
            return "";
        }

        return prefix;
    }
}
