package org.excelparser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * apply this at the member fields
 *
 * @author zaccoding
 * github : https://github.com/zacscoding
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
    /**
     * Cell Name
     */
    String cellValue() default "";

    /**
     * ClassType
     * - Primitive : java primitive + String
     * - Object : others
     */
    ExcelFieldType fieldType() default ExcelFieldType.Primitive;

    /**
     * Cell order (start :: 0)
     */
    int cellOrder();

    /**
     * Cell null able
     */
    boolean notNull() default false;

    /**
     * Regex
     */
    String regex() default "";

    /**
     * value inovke methods with no params
     * {"setter method name" , "getter method name"}
     */
    String[] valueInvokeMethod() default {"", ""};
}