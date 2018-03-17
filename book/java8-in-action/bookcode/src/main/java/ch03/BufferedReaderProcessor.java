package ch03;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Step 2 : Use a functional interface to pass behaviors
 *
 * @author zacconding
 * @Date 2018-03-18
 * @GitHub : https://github.com/zacscoding
 */
@FunctionalInterface
public interface BufferedReaderProcessor {

    String process(BufferedReader br) throws IOException;
}
