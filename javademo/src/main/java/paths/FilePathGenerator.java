package paths;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple file path generator
 *
 * @GitHub : https://github.com/zacscoding
 */
public class FilePathGenerator {

    private static final char PATH_DELIMITER = '/';
    private static final Pattern NAMES_PATTERN = Pattern.compile("\\{([^/]+?)\\}");

    private String path;
    private Map<String, ?> pathVariables;

    public static Builder builder() {
        return new Builder();
    }

    public static Builder fromPath(String path) {
        return new Builder().path(path);
    }

    private FilePathGenerator(String path, Map<String, ?> pathVariables) {
        this.path = getPath(path);
        this.pathVariables = pathVariables;
    }

    /**
     * Replace path variable in path and append path segments
     */
    public String toFilePath() {
        if (path.indexOf('{') == -1) {
            return path;
        }

        Matcher matcher = NAMES_PATTERN.matcher(path);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue = getVariableValue(variableName);

            matcher.appendReplacement(sb, variableValue);
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Getting path variable value from pathVariables map
     *
     * @return "" if value is null or not contains, otherwise value.toString()
     */
    private String getVariableValue(String variableName) {
        if (pathVariables == null || pathVariables.isEmpty()) {
            return "";
        }

        Object value = pathVariables.get(variableName);
        return value == null ? "" : value.toString();
    }

    /**
     * Getting path with starts PATH_DELIMITER
     *
     * @return "" if path is null, otherwise path with started PATH_DELIMITER
     */
    private String getPath(String path) {
        if (path == null) {
            return "";
        }

        if (path.charAt(0) != PATH_DELIMITER) {
            return PATH_DELIMITER + path;
        }

        return path;
    }

    public static class Builder {

        private String path;
        private Map<String, Object> pathVariables;

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder pathVariable(String key, Object value) {
            if (pathVariables == null) {
                pathVariables = new HashMap<>();
            }
            pathVariables.put(key, value);
            return this;
        }

        public Builder pathVariables(Map<String, Object> pathVariables) {
            if (pathVariables == null) {
                return this;
            }

            if (this.pathVariables == null) {
                this.pathVariables = pathVariables;
            } else {
                this.pathVariables.putAll(pathVariables);
            }

            return this;
        }

        public FilePathGenerator build() {
            return new FilePathGenerator(path, pathVariables);
        }
    }
}