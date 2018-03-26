package annotation;

import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.Test;
import util.ModifyAnnotationValue;
import util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-03-26
 * @GitHub : https://github.com/zacscoding
 */
public class ChangeAnnotationValueTest {

    @Test
    public void modifyAnnotationTest() {
        // ==================== origin ====================
        // value() : default-value , intValue() : 1
        ChangeValueAnnotation origin = ChangeAnnotationTestDomain.class.getAnnotation(ChangeValueAnnotation.class);
        SimpleLogger.build().appendRepeat(10, "==").append(" origin ").appendRepeat(10, "==").newLine()
                    .appendln("value() : {} , intValue() : {}", origin.value(), origin.intValue()).flush();

        ChangeValueAnnotation modify = new ChangeValueAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return origin.annotationType();
            }

            @Override
            public String value() {
                return "modified value";
            }

            @Override
            public int intValue() {
                return origin.intValue();
            }
        };

        assertTrue(ModifyAnnotationValue.modifyAnnotationValue(ChangeAnnotationTestDomain.class, ChangeValueAnnotation.class, modify));

        // ==================== new annotation ====================
        // value() : modified value , intValue() : 1
        ChangeValueAnnotation newAnnotation = ChangeAnnotationTestDomain.class.getAnnotation(ChangeValueAnnotation.class);
        SimpleLogger.build().appendRepeat(10, "==").append(" new annotation ").appendRepeat(10, "==").newLine()
                    .appendln("value() : {} , intValue() : {}", newAnnotation.value(), newAnnotation.intValue()).flush();
    }
}

@ChangeValueAnnotation
class ChangeAnnotationTestDomain {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface ChangeValueAnnotation {

    String value() default "default-value";

    int intValue() default 1;
}




