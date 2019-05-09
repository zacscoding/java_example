package demo.generator;

import demo.generator.annotation.ArgOption;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Simple command generator about @Parameter and @DynamicParameter of JCommander
 *
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleCommandLineGenerator<T extends CommandLineGenerative> implements CommandGenerator {

    private T object;
    private List<ArgOptionDescription> descriptions;

    public static Builder newBuilder() {
        return new Builder();
    }

    public SimpleCommandLineGenerator(T object) {
        this.object = Objects.requireNonNull(object, "object must be not null");
    }

    @Override
    public String generateCommandLine() throws IllegalAccessException {
        parseArgs();

        StringBuilder commandLine = new StringBuilder(object.getPrefix());

        // append args
        boolean firstArg = true;
        for (ArgOptionDescription description : descriptions) {
            ArgOption argOption = description.getArgOption();
            Field field = description.getField();

            String value = getValue(argOption, field);
            if (value.isEmpty()) {
                continue;
            }

            if (!firstArg) {
                commandLine.append(" ");
            }

            commandLine.append(argOption.opt())
                .append(" ")
                .append(value);

            firstArg = false;
        }

        return commandLine.toString();
    }

    @Override
    public String usage() {
//        parseArgs();
//
//        String commandDescription = object.getDescription();
//        String usage = object.getDescription();
//
//        StringBuilder builder = new StringBuilder();
//
//        if (commandDescription != null) {
//            builder.append(commandDescription)
//                .append("\n");
//        }
//
//        if (usage != null) {
//            builder.append("Usage : ")
//                .append(usage);
//        }
//
//        for (ArgOptionDescription description : descriptions) {
//            ArgOption argOption = description.getArgOption();
//
//            if (!argOption.required()) {
//                builder.append("[");
//            }
//        }

        throw new UnsupportedOperationException("Will added");
    }

    /**
     * Extract @ArgOption annotation from fields
     */
    private void parseArgs() {
        if (descriptions != null) {
            return;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        if (fields == null || fields.length == 0) {
            descriptions = Collections.emptyList();
        }

        descriptions = new ArrayList<>(fields.length);

        for (Field field : clazz.getDeclaredFields()) {
            ArgOption annotation = field.getAnnotation(ArgOption.class);
            if (annotation == null) {
                continue;
            }

            descriptions.add(new ArgOptionDescription(field, annotation));
        }
    }

    /**
     * Gettring value from field and Object
     */
    private String getValue(ArgOption argOption, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object obj = field.get(object);

        if (obj != null) {
            return obj.toString();
        }

        if (argOption.required()) {
            throw new IllegalArgumentException(
                String.format("%s is required to generate cli but this is null", field.getName())
            );
        }

        return "";
    }


    /**
     * Builder of SimpleCommandGenerator
     */
    public static class Builder<T extends CommandLineGenerative> {

        private T object;

        public Builder object(T object) {
            this.object = object;
            return this;
        }

        public SimpleCommandLineGenerator build() {
            return new SimpleCommandLineGenerator(object);
        }
    }
}
