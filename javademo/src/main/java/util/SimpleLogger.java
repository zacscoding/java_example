package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import sun.applet.Main;
import sun.usagetracker.UsageTrackerClient;

/**
 * @author zacconding
 * @Date 2018-01-14
 * @GitHub : https://github.com/zacscoding
 */
public class SimpleLogger {

    private static final String NEW_LINE;
    private static Gson TO_SRING_GSON;
    public static final PrintStream PS = System.out;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {
        TO_SRING_GSON = new GsonBuilder().serializeNulls().create();
        NEW_LINE = System.getProperty("line.separator");
        SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyMMdd HH:mm:ss.SSS");
    }

    public static void info(String message, Object... args) {
        String prefix = SIMPLE_DATE_FORMAT.format(new Date()) + " : " + getClassName();
        println(prefix + " " + message, args);
    }

    public static void error(Throwable t) {
        error(null, t);
    }

    public static void error(String message, Throwable t) {
        println(SIMPLE_DATE_FORMAT.format(new Date()) + " : " + getClassName() + (message == null ? "" : message));
        if (t != null) {
            println(getStackTraceString(t));
        }
    }

    public static void print(String content) {
        PS.print(content);
    }

    public static void print(String content, Object... args) {
        PS.print(parseContent(content, args));
    }

    public static void println(String content) {
        PS.println(content);
    }

    public static void println(String content, Object... args) {
        PS.println(parseContent(content, args));
    }

    public static String toJSON(Object inst) {
        return TO_SRING_GSON.toJson(inst);
    }

    private static String parseContent(String content, Object[] args) {
        if (args == null || args.length == 0 || content == null || content.length() < 2) {
            return content;
        }

        int argIdx = 0;
        int length = content.length();
        StringBuilder sb = new StringBuilder(length > 16 ? length : 16);

        for (int i = 0; i < length; i++) {
            char curChar = content.charAt(i);
            if ((content.charAt(i) == '{') && (i + 1 < length) && (content.charAt(i + 1) == '}') && (isRange(args, argIdx))) {
                sb.append(args[argIdx++]);
                i++;
            } else {
                sb.append(curChar);
            }
        }

        return sb.toString();
    }

    private static boolean isRange(Object[] array, int idx) {
        if (idx < 0 || array == null || array.length <= idx) {
            return false;
        }
        return true;
    }

    private static String getClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    private static String getStackTraceString(Throwable t) {
        if (t == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        StackTraceElement[] elts = t.getStackTrace();
        if (elts != null) {
            for (int i = 0; i < elts.length; i++) {
                if (elts[i] != null) {
                    sb.append("\t").append(elts[i].toString());
                    if (i != elts.length - 1) {
                        sb.append(NEW_LINE);
                    }
                }
            }
        }

        return sb.toString();
    }
}
