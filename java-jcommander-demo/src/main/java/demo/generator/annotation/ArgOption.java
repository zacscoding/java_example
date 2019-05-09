package demo.generator.annotation;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @GitHub : https://github.com/zacscoding
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface ArgOption {

    String opt();

    String longOpt() default "";

    boolean required() default false;

    String desc() default "";
}
