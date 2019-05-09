package demo.generator;

import demo.generator.annotation.ArgOption;
import java.lang.reflect.Field;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@AllArgsConstructor
public class ArgOptionDescription {

    private Field field;
    private ArgOption argOption;
}
