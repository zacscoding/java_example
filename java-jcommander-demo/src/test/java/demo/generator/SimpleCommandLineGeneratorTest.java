package demo.generator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import demo.parameters.GenerateParameter;
import org.junit.Test;

/**
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleCommandLineGeneratorTest {

    @Test
    public void generateCli() throws IllegalAccessException {
        // given
        GenerateParameter parameter = GenerateParameter.builder()
            .intValue(1)
            .prefix("module")
            .stringValue("str")
            .inner(
                GenerateParameter.GenerateParameterInner.builder()
                    .ip("192.168.100.1")
                    .username("app")
                    .port(22)
                    .build()
            )
            .build();

        SimpleCommandLineGenerator cliGenerator = SimpleCommandLineGenerator.newBuilder().object(parameter).build();

        // when
        String cli = cliGenerator.generateCommandLine();
        System.out.println(cli);

        // then
        assertTrue(cli.startsWith("module"));
        assertTrue(cli.contains("-i " + parameter.getIntValue()));
        assertTrue(cli.contains("-s " + parameter.getStringValue()));
        assertTrue(cli.contains("-h " + parameter.getInner().toString()));
    }

    @Test(expected = NullPointerException.class)
    public void generateCliAndThenThrowNullPointExIfObjectIsNull() {
        SimpleCommandLineGenerator.newBuilder().object(null).build();
        fail();
    }

    @Test
    public void generateCliAndThenThrowIllegalArgEx() throws IllegalAccessException {
        try {
            SimpleCommandLineGenerator.newBuilder()
                .object(GenerateParameter.builder().build())
                .build()
                .generateCommandLine();
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void generateCliStartWithDefaultPrefixIfNull() throws IllegalAccessException {
        // given when
        String cli = SimpleCommandLineGenerator.newBuilder()
            .object(
                GenerateParameter.builder()
                    .prefix(null)
                    .stringValue("str")
                    .build()
            )
            .build()
            .generateCommandLine();

        // then
        assertThat(cli, is("-s str"));
    }
}
