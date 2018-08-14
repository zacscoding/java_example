package basic.bindingannotations;

import static java.lang.annotation.ElementType.PARAMETER;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
@BindingAnnotation
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PayPal {
}
