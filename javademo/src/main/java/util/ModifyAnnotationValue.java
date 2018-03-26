package util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Modify annotatios values
 *
 * ref : https://github.com/eugenp/tutorials/blob/master/core-java/src/main/java/com/baeldung/java/reflection/GreetingAnnotation.java
 *
 * @author zacconding
 * @Date 2018-03-26
 * @GitHub : https://github.com/zacscoding
 */
public class ModifyAnnotationValue {

    /**
     * valid in java 8
     */
    public static boolean modifyAnnotationValue(Class<?> target, Class<? extends Annotation> targetAnnotation, Annotation targetValue) {
        try {
            Method method = Class.class.getDeclaredMethod("annotationData");
            method.setAccessible(true);

            Object annotationData = method.invoke(target);

            Field annotations = annotationData.getClass().getDeclaredField("annotations");
            annotations.setAccessible(true);

            Map<Class<? extends Annotation>, Annotation> map = (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
            map.put(targetAnnotation, targetValue);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
