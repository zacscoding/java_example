package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zacconding
 * @Date 2018-02-05
 * @GitHub : https://github.com/zacscoding
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectionAnnotataion {

    String value() default "default-value";
}
